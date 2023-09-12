# 编译
{
    alias build="mvn install -Dmaven.test.skip=true &&
                 cp sonar-pmd-plugin/target/sonar-pmd-plugin-3.3.0.2.jar /media/sf_share"
}

# 扫描
{
    pmd check \
        --dir=sonar-pmd-plugin/src/main/java \
        --aux-classpath=sonar-pmd-plugin/src/main/java/classes \
        --rulesets=rulesets/java/quickstart.xml \
        --format=text
}

# 资料
{
    https://www.mikesay.com/2021/01/17/sonar-plugin-principle/
}

mvn install -Dmaven.test.skip=true && cp sonar-pmd-plugin/target/sonar-pmd-plugin-3.3.0.2.jar /media/sf_share
