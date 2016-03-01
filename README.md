# android-get-process
获取其它应用的运行状态进程，在Android5.0上面不能Android的api不在支持获取其它应用的运行进程，只能获取自己的进程，在[jaredrummler/AndroidProcesses](https://github.com/jaredrummler/AndroidProcesses)的基础上面，使用了ps命令解析。

# 使用方法
`ProcessChecker.isCheckProcess(packName,this.getApplicationContext());`
true：表示包名packName应用进程已运行；  
false：表示包名packName应用进程未运行。
