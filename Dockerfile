# Docker Imageとしてadoptopenjdk11を使用
FROM adoptopenjdk:11-jdk-hotspot

# git のインストール
RUN apt-get update && apt-get install -y \
       wget tar iproute2 git

# mavenのインストール
RUN apt-get install -y maven

# PJのコピー
RUN git clone https://github.com/MisakiFujishiro/sqs_consumer

# プロジェクトのビルド
RUN mvn install -DskipTests=true -f /sqs_consumer/pom.xml

# タイムゾーンの変更
RUN ln -sf  /usr/share/zoneinfo/Asia/Tokyo /etc/localtime

# コンテナのポート解放
EXPOSE 8080

# Javaの実行
CMD java -jar sqs_consumer/target/sqs_consumer-0.0.1-SNAPSHOT.jar --spring.profiles.active=standard
