REGISTRY    	:= registry.hub.docker.com/kenshin579
REPOSITORY 		:= advenoh
APP    			:= app-quotes-server

VERSION     	:= 0.0.1
TIMESTAMP   	:= $(shell TZ="Asia/Seoul" date +"%m%d%H%M")

TAG         	:= $(APP)-$(VERSION)-$(TIMESTAMP)
TAG_RPI         := $(APP)-$(VERSION)-rpi-$(TIMESTAMP)
IMAGE       	:= $(REGISTRY)/$(REPOSITORY):$(TAG)
IMAGE_RPI       := $(REGISTRY)/$(REPOSITORY):$(TAG_RPI)

.PHONY: docker-build-rpi
docker-build-rpi:
	@docker buildx build \
	--platform linux/arm/v7,linux/arm/v6 \
	-t $(IMAGE_RPI) --push -f Dockerfile.rpi .

.PHONY: docker-push-rpi
docker-push-rpi: docker-build-rpi
	@docker login --username $(DOCKER_USERNAME) --password $(DOCKER_PASSWORD) $(REGISTRY)
	@docker push $(IMAGE_RPI)


.PHONY: docker-build
docker-build:
	@docker build -t $(IMAGE) -f Dockerfile.mac .

.PHONY: docker-push
docker-push: docker-build
	@docker login --username $(DOCKER_USERNAME) --password $(DOCKER_PASSWORD) $(REGISTRY)
	@docker push $(IMAGE)