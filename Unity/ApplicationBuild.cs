using UnityEditor;
using UnityEngine;
using System;
using System.Linq;

public static class ApplicationBuild {

    private static string[] GetAllScenePaths() {
        return EditorBuildSettings.scenes
            .Where(scene => scene.enabled)
            .Select(scene => scene.path)
            .ToArray();
    }

    public static void AndroidBuild() {

        string[] scenes = GetAllScenePaths();
        BuildPipeline.BuildPlayer(scenes, "../temp/", BuildTarget.Android, BuildOptions.AcceptExternalModificationsToPlayer);
        
    }

}