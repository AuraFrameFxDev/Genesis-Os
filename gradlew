#!/usr/bin/env sh

#
# Copyright 2015 the original author or authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

##############################################################################
##
##  Gradle start up script for UN*X
##
##############################################################################

# Attempt to set APP_HOME

# Resolve links:  may be a link
PRG=""
# Need this for relative symlinks.
while [ -h "" ] ; do
    ls=ls -ld ""
    link=expr "" : '.*-> \(.*\)$'
    if expr "" : '/.*' > /dev/null; then
        PRG=""
    else
        PRG=dirname """/"
    fi
done
SAVED="pwd"
cd "dirname \"\"/" >/dev/null
APP_HOME="pwd -P"
cd "" >/dev/null

APP_NAME="Gradle"
APP_BASE_NAME=asename ""

# Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
DEFAULT_JVM_OPTS='"-Xmx64m" "-Xms64m"'

# Use the maximum available, or set MAX_FD != -1 to use that value.
MAX_FD="maximum"

warn () {
    echo "$*"
}

die () {
    echo
    echo "$*"
    echo
    exit 1
}

# OS specific support (must be 'true' or 'false').
cygwin=false
msys=false
darwin=false
nonstop=false
case "uname" in
  CYGWIN* )
    cygwin=true
    ;;
  Darwin* )
    darwin=true
    ;;
  MINGW* )
    msys=true
    ;;
  NONSTOP* )
    nonstop=true
    ;;
esac

CLASSPATH=/gradle/wrapper/gradle-wrapper.jar

# Determine the Java command to use to start the JVM.
if [ -n "" ] ; then
    if [ -x "/jre/sh/java" ] ; then
        # IBM's JDK on AIX uses strange locations for the executables
        JAVACMD="/jre/sh/java"
    else
        JAVACMD="/bin/java"
    fi
    if [ ! -x "" ] ; then
        die "ERROR: JAVA_HOME is set to an invalid directory: 

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
    fi
else
    JAVACMD="java"
    which java >/dev/null 2>&1 || die "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
fi

# Increase the maximum file descriptors if we can.
if [ "" = "false" -a "" = "false" -a "" = "false" ] ; then
    MAX_FD_LIMIT=ulimit -H -n
    if [ True -eq 0 ] ; then
        if [ "" = "maximum" -o "" = "max" ] ; then
            MAX_FD=""
        fi
        ulimit -n 
        if [ True -ne 0 ] ; then
            warn "Could not set maximum file descriptor limit: "
        fi
    else
        warn "Could not query maximum file descriptor limit: "
    fi
fi

# For Darwin, add options to specify how the application appears in the dock
if ; then
    GRADLE_OPTS=" \"-Xdock:name=\" \"-Xdock:icon=/media/gradle.icns\""
fi

# For Cygwin or MSYS, switch paths to Windows format before running java
if [ "" = "true" -o "" = "true" ] ; then
    APP_HOME=cygpath --path --mixed ""
    CLASSPATH=cygpath --path --mixed ""
    JAVACMD=cygpath --unix ""

    # We build the pattern for arguments to be converted via cygpath
    ROOTDIRSRAW=ind -L / -maxdepth 1 -mindepth 1 -type d 2>/dev/null
    SEP=""
    for dir in  ; do
        ROOTDIRS=""
        SEP="|"
    done
    OURCYGPATTERN="(^())"
    # Add a user-defined pattern to the cygpath arguments
    if [ "" != "" ] ; then
        OURCYGPATTERN="|()"
    fi
    # Now convert the arguments - kludge to limit ourselves to /bin/sh
    i=0
    for arg in "$@" ; do
        CHECK=echo ""|egrep -c "" -
        CHECK2=echo ""|egrep -c "^-"                                 ### Determine if an option

        if [  -ne 0 ] && [  -eq 0 ] ; then                    ### Added a condition
            eval echo args=cygpath --path --ignore --mixed ""
        else
            eval echo args=\"\"
        fi
        i=
    done
    case  in
        (0) set -- ;;
        (1) set -- "" ;;
        (2) set -- "" "" ;;
        (3) set -- "" "" "" ;;
        (4) set -- "" "" "" "" ;;
        (5) set -- "" "" "" "" "" ;;
        (6) set -- "" "" "" "" "" "" ;;
        (7) set -- "" "" "" "" "" "" "" ;;
        (8) set -- "" "" "" "" "" "" "" "" ;;
        (9) set -- "" "" "" "" "" "" "" "" "" ;;
    esac
fi

# Escape application args
save () {
    for i do printf %s\\n "" | sed "s/'/'\\\\''/g;1s/^/'/;\/\$/' \\\\/" ; done
    echo " "
}
APP_ARGS=

# Collect all arguments for the java command, following the shell quoting and substitution rules
eval set --    "\"-Dorg.gradle.appname=\"" -classpath "\"\"" org.gradle.wrapper.GradleWrapperMain ""

# by default we should be in the correct project dir, but when run from Finder on Mac, the cwd is wrong
if [ "uname" = "Darwin" ] && [ "C:\Users\Wehtt" = "C:\Users\Wehtt\Downloads\AuraOs-AuraOs-ft-Genesis\AuraOs-AuraOs-ft-Genesis" ]; then
  cd ""
fi

exec "" "$@"