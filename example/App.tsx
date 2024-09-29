import React from "react";
import { Button, StyleSheet, View } from "react-native";
import * as ReactNativeFloatingWidget from "react-native-floating-widget";

export default function App() {
  return (
    <View style={styles.container}>
      <Button
        title="Check Permission"
        onPress={() =>
          ReactNativeFloatingWidget.checkPermissionAsync()
            .then(console.log)
            .catch(console.log)
        }
      />
      <Button
        title="Open Permission"
        onPress={() => {
          ReactNativeFloatingWidget.requestPermissionAsync()
            .then(console.log)
            .catch(console.log);
        }}
      />

      <Button title="Start" onPress={() => ReactNativeFloatingWidget.start()} />
      <Button title="Stop" onPress={() => ReactNativeFloatingWidget.stop()} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#fff",
    alignItems: "center",
    justifyContent: "space-around",
  },
});
