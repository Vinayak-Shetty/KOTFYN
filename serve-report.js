const http = require("http");
const fs = require("fs");
const path = require("path");

const [, , reportDirArg, portArg] = process.argv;

if (!reportDirArg || !portArg) {
  console.error("Usage: node serve-report.js <reportDir> <port>");
  process.exit(1);
}

const root = path.resolve(reportDirArg);
const port = Number(portArg);

const contentTypes = {
  ".html": "text/html; charset=utf-8",
  ".js": "application/javascript; charset=utf-8",
  ".css": "text/css; charset=utf-8",
  ".json": "application/json; charset=utf-8",
  ".png": "image/png",
  ".jpg": "image/jpeg",
  ".jpeg": "image/jpeg",
  ".svg": "image/svg+xml",
  ".woff": "font/woff",
  ".woff2": "font/woff2",
};

function send(response, statusCode, body, contentType = "text/plain; charset=utf-8") {
  response.writeHead(statusCode, { "Content-Type": contentType });
  response.end(body);
}

function escapeHtml(value) {
  return String(value ?? "")
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;")
    .replace(/'/g, "&#39;");
}

function getDataRoot() {
  return fs.existsSync(path.join(root, "widgets")) ? root : path.join(root, "awesome");
}

function readJson(relativePath, fallback) {
  const dataRoot = getDataRoot();
  try {
    return JSON.parse(fs.readFileSync(path.join(dataRoot, relativePath), "utf8").replace(/^\uFEFF/, ""));
  } catch {
    return fallback;
  }
}

function statusClass(status) {
  return escapeHtml(status || "unknown");
}

function renderAttachments(attachments) {
  const links = (attachments || [])
    .map((attachment) => attachment.link)
    .filter(Boolean)
    .map((link) => {
      const label = link.name || "Attachment";
      const href = `/data/attachments/${escapeHtml(link.id)}${escapeHtml(link.ext || "")}`;
      return `<a href="${href}" target="_blank" rel="noopener">${escapeHtml(label)}</a>`;
    });

  return links.length ? `<div class="attachments">${links.join("")}</div>` : "";
}

function renderSteps(steps, level = 0) {
  if (!Array.isArray(steps) || steps.length === 0) {
    return "";
  }

  return `<ol class="steps level-${level}">${steps.map((step) => {
    const error = step.error || {};
    const message = step.message || error.message || "";
    const trace = step.trace || error.trace || "";
    return `<li class="step ${statusClass(step.status)}">
      <div class="step-line">
        <span class="status">${escapeHtml(step.status || "unknown")}</span>
        <span class="step-name">${escapeHtml(step.name || step.type || "Step")}</span>
        <span class="duration">${escapeHtml(step.duration || 0)} ms</span>
      </div>
      ${message ? `<pre class="message">${escapeHtml(message)}</pre>` : ""}
      ${trace ? `<details><summary>Stack trace</summary><pre>${escapeHtml(trace)}</pre></details>` : ""}
      ${renderAttachments(step.attachments)}
      ${renderSteps(step.steps, level + 1)}
    </li>`;
  }).join("")}</ol>`;
}

function renderFixtureGroup(title, fixtures) {
  if (!Array.isArray(fixtures) || fixtures.length === 0) {
    return "";
  }

  return `<details class="fixture" open>
    <summary>${escapeHtml(title)}</summary>
    ${renderSteps(fixtures.map((fixture) => ({
      name: fixture.name,
      status: fixture.status,
      duration: fixture.duration,
      steps: fixture.steps,
      attachments: fixture.attachments,
      error: fixture.error,
      message: fixture.message,
      trace: fixture.trace,
    })))}
  </details>`;
}

function renderDetailedReport() {
  const tree = readJson(path.join("widgets", "tree.json"), { leavesById: {} });
  const stats = readJson(path.join("widgets", "statistic.json"), {});
  const summary = readJson("summary.json", {});
  const leaves = Object.values(tree.leavesById || {}).sort((a, b) => (a.groupOrder || 0) - (b.groupOrder || 0));
  const reportTitle = summary.name || "Automation Report";
  const durationMs = summary.duration || leaves.reduce((total, leaf) => total + (leaf.duration || 0), 0);
  const durationText = durationMs > 1000 ? `${(durationMs / 1000).toFixed(1)} s` : `${durationMs} ms`;

  const nav = leaves.map((leaf) => `
    <a class="nav-item ${statusClass(leaf.status)}" href="#${escapeHtml(leaf.nodeId)}">
      <span class="nav-title">${escapeHtml(leaf.name || "")}</span>
      <span class="nav-meta">
        <strong>${escapeHtml(leaf.status || "")}</strong>
        <small>${escapeHtml(leaf.duration || 0)} ms</small>
      </span>
    </a>
  `).join("");

  const rows = leaves.map((leaf) => {
    const detail = readJson(path.join("data", "test-results", `${leaf.nodeId}.json`), {});
    const error = detail.error || {};
    const failedStep = (detail.steps || []).find((step) => step.status && step.status !== "passed") || {};
    const message = error.message || failedStep.message || "";
    const trace = error.trace || failedStep.trace || "";
    const labels = (detail.labels || []).map((label) => `<span>${escapeHtml(label.name)}: ${escapeHtml(label.value)}</span>`).join("");

    return `
      <section class="test ${statusClass(leaf.status)}" id="${escapeHtml(leaf.nodeId)}">
        <div class="test-header">
          <div>
            <span class="status">${escapeHtml(detail.status || leaf.status || "")}</span>
            <h2>${escapeHtml(detail.name || leaf.name || "")}</h2>
            <p>${escapeHtml(detail.fullName || "")}</p>
          </div>
          <span class="duration">${escapeHtml(detail.duration || leaf.duration || 0)} ms</span>
        </div>
        <div class="labels">${labels}</div>
        ${message ? `<pre class="message">${escapeHtml(message)}</pre>` : ""}
        ${trace ? `<details><summary>Stack trace</summary><pre>${escapeHtml(trace)}</pre></details>` : ""}
        ${renderAttachments(detail.attachments)}
        ${renderFixtureGroup("Setup", detail.setup)}
        <h3>Steps</h3>
        ${renderSteps(detail.steps) || "<p>No steps recorded.</p>"}
        ${renderFixtureGroup("Teardown", detail.teardown)}
      </section>
    `;
  }).join("");

  return `<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <title>Detailed Test Report</title>
  <style>
    * { box-sizing: border-box; }
    :root {
      --bg: #eef3f8;
      --panel: #ffffff;
      --panel-soft: #f8fafc;
      --line: #d9e2ec;
      --text: #102033;
      --muted: #5f6f82;
      --blue: #2563eb;
      --green: #17855a;
      --red: #c9362b;
      --gray: #7a8698;
    }
    html { scroll-behavior: smooth; }
    body { margin: 0; font: 14px/1.45 "Segoe UI", Arial, sans-serif; background: var(--bg); color: var(--text); }
    header { position: sticky; top: 0; z-index: 2; background: rgba(255,255,255,.96); border-bottom: 1px solid var(--line); padding: 16px 24px; backdrop-filter: blur(8px); }
    h1 { margin: 0; font-size: 24px; font-weight: 750; }
    h2 { margin: 4px 0; font-size: 20px; line-height: 1.25; }
    h3 { margin: 18px 0 10px; font-size: 14px; text-transform: uppercase; letter-spacing: .04em; color: var(--muted); }
    .topline { display: flex; align-items: end; justify-content: space-between; gap: 20px; margin-bottom: 14px; }
    .subtitle { margin: 4px 0 0; color: var(--muted); }
    .layout { display: grid; grid-template-columns: 390px minmax(0, 1fr); min-height: calc(100vh - 122px); }
    nav { position: sticky; top: 122px; height: calc(100vh - 122px); overflow: auto; border-right: 1px solid var(--line); background: #f9fbfd; padding: 14px; }
    main { padding: 24px 30px 44px; min-width: 0; }
    .stats { display: grid; grid-template-columns: repeat(6, minmax(96px, 1fr)); gap: 10px; }
    .pill { border: 1px solid var(--line); border-radius: 7px; padding: 10px 12px; background: var(--panel); box-shadow: 0 1px 2px rgba(16,32,51,.04); }
    .pill small { display: block; color: var(--muted); font-size: 11px; text-transform: uppercase; letter-spacing: .04em; }
    .pill strong { display: block; margin-top: 2px; font-size: 18px; }
    .nav-item { display: block; text-decoration: none; color: var(--text); border: 1px solid var(--line); border-left-width: 5px; border-radius: 7px; padding: 11px 12px; margin-bottom: 9px; background: var(--panel); box-shadow: 0 1px 2px rgba(16,32,51,.03); }
    .nav-item:hover, .test:target { border-color: var(--blue); box-shadow: 0 6px 18px rgba(37,99,235,.12); }
    .nav-title { display: block; font-weight: 650; line-height: 1.35; }
    .nav-meta { display: flex; align-items: center; gap: 10px; margin-top: 8px; color: var(--muted); }
    .nav-meta strong { border-radius: 999px; padding: 2px 8px; font-size: 11px; text-transform: uppercase; background: #e8f5ef; color: var(--green); }
    .test, .step { border-left-width: 5px; }
    .passed { border-left-color: var(--green); }
    .failed, .broken { border-left-color: var(--red); }
    .skipped { border-left-color: var(--gray); }
    .unknown { border-left-color: var(--muted); }
    .test { background: var(--panel); border: 1px solid var(--line); border-left-width: 6px; border-radius: 8px; margin: 0 0 18px; padding: 20px; scroll-margin-top: 144px; box-shadow: 0 8px 24px rgba(16,32,51,.06); }
    .test-header { display: flex; align-items: start; justify-content: space-between; gap: 12px; }
    .test-header p { margin: 0; color: #637083; word-break: break-word; }
    .status { display: inline-block; text-transform: uppercase; font-weight: 750; font-size: 12px; color: var(--green); }
    .duration { color: var(--muted); white-space: nowrap; }
    .labels { display: flex; flex-wrap: wrap; gap: 6px; margin: 12px 0; }
    .labels span { border: 1px solid var(--line); border-radius: 999px; padding: 4px 9px; color: #42526b; background: var(--panel-soft); font-size: 12px; }
    .steps { margin: 0; padding-left: 18px; }
    .step { border: 1px solid var(--line); border-left-width: 5px; border-radius: 7px; margin: 9px 0; padding: 11px 12px; background: var(--panel-soft); }
    .step-line { display: grid; grid-template-columns: 80px 1fr auto; gap: 10px; align-items: center; }
    .step-name { font-weight: 600; word-break: break-word; }
    .fixture { margin-top: 14px; }
    .fixture > summary { cursor: pointer; font-weight: 700; }
    pre { white-space: pre-wrap; word-break: break-word; background: #101828; color: #f8fafc; border-radius: 7px; padding: 12px; overflow: auto; }
    .message { color: #8b1a10; background: #fff2f0; }
    .attachments { display: flex; flex-wrap: wrap; gap: 8px; margin: 10px 0; }
    .attachments a { border: 1px solid #b9c7dc; border-radius: 6px; padding: 6px 9px; background: #fff; }
    a { color: var(--blue); }
    @media (max-width: 900px) {
      header { position: static; }
      .topline { align-items: start; flex-direction: column; }
      .stats { grid-template-columns: repeat(2, 1fr); }
      .layout { grid-template-columns: 1fr; }
      nav { position: static; height: auto; border-right: 0; border-bottom: 1px solid #dfe5ef; }
      .step-line { grid-template-columns: 1fr; gap: 2px; }
    }
  </style>
</head>
<body>
  <header>
    <div class="topline">
      <div>
        <h1>${escapeHtml(reportTitle)}</h1>
        <p class="subtitle">RegistrationSmokeTest detailed execution report</p>
      </div>
      <div class="subtitle">Duration ${escapeHtml(durationText)}</div>
    </div>
    <div class="stats">
      <span class="pill"><small>Total</small><strong>${escapeHtml(stats.total || 0)}</strong></span>
      <span class="pill"><small>Passed</small><strong>${escapeHtml(stats.passed || 0)}</strong></span>
      <span class="pill"><small>Failed</small><strong>${escapeHtml(stats.failed || 0)}</strong></span>
      <span class="pill"><small>Broken</small><strong>${escapeHtml(stats.broken || 0)}</strong></span>
      <span class="pill"><small>Skipped</small><strong>${escapeHtml(stats.skipped || 0)}</strong></span>
      <span class="pill"><small>Duration</small><strong>${escapeHtml(durationText)}</strong></span>
    </div>
  </header>
  <div class="layout">
    <nav>${nav || "<p>No test results found.</p>"}</nav>
    <main>${rows || "<p>No test results found.</p>"}</main>
  </div>
</body>
</html>`;
}

const server = http.createServer((request, response) => {
  try {
    const url = new URL(request.url, `http://${request.headers.host}`);

    if (url.pathname === "/details.html" || url.pathname === "/summary.html") {
      send(response, 200, renderDetailedReport(), "text/html; charset=utf-8");
      return;
    }

    if ((url.pathname === "/" || url.pathname === "/index.html") && fs.existsSync(path.join(root, "awesome", "index.html"))) {
      response.writeHead(302, {
        Location: "/awesome/index.html#/",
        "Cache-Control": "no-store",
      });
      response.end();
      return;
    }

    let requestPath = decodeURIComponent(url.pathname === "/" ? "/index.html" : url.pathname);

    if (requestPath.startsWith("/allure-maven/")) {
      requestPath = `/awesome/${requestPath.slice("/allure-maven/".length)}`;
    }

    let filePath = path.resolve(root, `.${requestPath}`);

    if (!filePath.startsWith(root)) {
      send(response, 403, "Forbidden");
      return;
    }

    if (!fs.existsSync(filePath) || !fs.statSync(filePath).isFile()) {
      const awesomePath = path.resolve(root, "awesome", `.${requestPath}`);

      if (!awesomePath.startsWith(root) || !fs.existsSync(awesomePath) || !fs.statSync(awesomePath).isFile()) {
        send(response, 404, "Not found");
        return;
      }

      filePath = awesomePath;
    }

    const extension = path.extname(filePath).toLowerCase();
    response.writeHead(200, {
      "Content-Type": contentTypes[extension] || "application/octet-stream",
      "Cache-Control": "no-store",
    });
    fs.createReadStream(filePath).pipe(response);
  } catch (error) {
    send(response, 500, error.message);
  }
});

server.listen(port, "127.0.0.1", () => {
  console.log(`Serving ${root} at http://127.0.0.1:${port}/index.html`);
});
