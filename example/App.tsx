import React, { useEffect } from "react";
import { AppState, StyleSheet, Text, View } from "react-native";
import * as ReactNativeFloatingWidget from "react-native-floating-widget";

export default function App() {
  useEffect(() => {
    ReactNativeFloatingWidget.requestPermissionAsync().then((result) => {
      console.log("Widget permission result", result);
    });
  }, []);
  useEffect(() => {
    const subscription = AppState.addEventListener("change", (nextAppState) => {
      console.log("AppState changed to", nextAppState);

      if (nextAppState === "active") {
        console.log("App has come to foreground");
        ReactNativeFloatingWidget.stop();
      } else if (nextAppState === "background") {
        console.log("App has come to background");
        ReactNativeFloatingWidget.start();
      }
    });
    return () => subscription.remove();
  }, []);
  return (
    <View style={styles.container}>
      <Text>Open up App.tsx to start working on your app!</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#fff",
    alignItems: "center",
    justifyContent: "center",
  },
});
