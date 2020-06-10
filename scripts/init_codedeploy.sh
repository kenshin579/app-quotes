#!/bin/bash

# install codedeploy agent
aws s3 cp s3://aws-codedeploy-ap-northeast-2/latest/install . --region ap-northeast-2
chmod +x ./install
sudo ./install auto

sudo service codedeploy-agent status
