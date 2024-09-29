import React from "react";
import { StyleSheet, View, Button } from "react-native";
import {
  checkPermissionAsync,
  requestPermissionAsync,
  start,
  stop,
} from "react-native-floating-widget";

export default function App() {
  return (
    <View style={styles.container}>
      <Button
        title="Check Permission"
        onPress={() =>
          checkPermissionAsync().then(console.log).catch(console.log)
        }
      />
      <Button
        title="Open Permission"
        onPress={() => {
          requestPermissionAsync().then(console.log).catch(console.log);
        }}
      />
      <Button title="Start" onPress={() => start()} />
      <Button title="Stop" onPress={() => stop()} />
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
