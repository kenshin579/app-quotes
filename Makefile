REGISTRY 	:= kenshin579
APP    		:= advenoh
APP_RPI    	:= rasberrypi
TAG         := app-quotes-server
IMAGE       := $(REGISTRY)/$(APP):$(TAG)

# rasberry
#	--platform linux/amd64,linux/arm/v7,linux/arm/v6
.PHONY: docker-push-rpi
docker-push-rpi:
	@docker buildx build \
	--platform linux/arm/v7,linux/arm/v6 \
	-t $(IMAGE) --push -f Dockerfile.rpi .

.PHONY: docker-build
docker-build:
	@docker build -t $(IMAGE) -f Dockerfile.mac .

.PHONY: docker-push
docker-push: docker-build
	@docker push $(IMAGE)