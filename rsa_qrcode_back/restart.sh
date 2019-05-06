#!/bin/bash

# ---------------------------------------------------------------------------
#
# 使用说明：
#
# 1: 本脚本仅仅通过调用 stop.sh 与 start.sh 实现重启
#
# 2：要特别注意 stop.sh 脚本中有关 MAIN_CLASS 配置的注意事项，
#    只有先确保 stop.sh 可以正常工作时才能使用该脚本 
#
# ---------------------------------------------------------------------------

# 得到基础路径
APP_BASE_PATH=$(cd `dirname $0`; pwd)

${APP_BASE_PATH}/stop.sh && ${APP_BASE_PATH}/start.sh
