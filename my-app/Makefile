REGISTRY 	:= kenshin579
APP    		:= advenoh
APP_RPI    	:= rasberrypi
TAG         := app-quotes-client
IMAGE       := $(REGISTRY)/$(APP):$(TAG)

.PHONY: docker-push-rpi
docker-push-rpi:
	@docker buildx build \
	--platform linux/arm/v7 \
	-t $(IMAGE) --push -f Dockerfile.rpi .

.PHONY: docker-build
docker-build:
	@docker build -t $(IMAGE) -f Dockerfile.mac .

.PHONY: docker-push
docker-push: docker-build
	@docker push $(IMAGE)