import { ConfigPlugin, withDangerousMod } from "@expo/config-plugins";
import fs from "fs";
import path from "path";

const withAndroidAddRes: ConfigPlugin<{
  resourcesPath: string[];
}> = (config, { resourcesPath }) => {
  return withDangerousMod(config, [
    "android",
    async (config) => {
      const {
        modRequest: { projectRoot },
      } = config;
      const androidResPath = path.join(
        projectRoot,
        "android/app/src/main/res/raw/"
      );
      const androidResPathExists = fs.existsSync(androidResPath);
      if (!androidResPathExists) {
        fs.mkdirSync(androidResPath, { recursive: true });
      }
      resourcesPath.forEach((resourcePath) => {
        fs.copyFileSync(
          resourcePath,
          androidResPath + path.basename(resourcePath)
        );
      });
      return config;
    },
  ]);
};

export default withAndroidAddRes;
