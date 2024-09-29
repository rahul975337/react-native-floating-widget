import ReactNativeFloatingWidgetModule from "./ReactNativeFloatingWidgetModule";

function checkPermissionAsync(): Promise<boolean> {
  return ReactNativeFloatingWidgetModule.checkPermissionAsync();
}

function requestPermissionAsync(): Promise<boolean> {
  return ReactNativeFloatingWidgetModule.requestPermissionAsync();
}

function start(): boolean {
  return ReactNativeFloatingWidgetModule.start();
}

function stop(): boolean {
  return ReactNativeFloatingWidgetModule.stop();
}

export { start, stop, requestPermissionAsync, checkPermissionAsync };
