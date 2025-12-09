import { bootstrapApplication } from '@angular/platform-browser';
import { App } from './app/app';
import { config } from './app/app.config.server';

// Accept an optional server bootstrap context and forward it to bootstrapApplication.
// The server renderer will pass a context object; forwarding it ensures the
// platform is created per-request and avoids NG0401: Missing Platform.
const bootstrap = (context?: unknown) => bootstrapApplication(App, config, context as any);

export default bootstrap;
