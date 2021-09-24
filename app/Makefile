REGISTRY 	:= kenshin579
APP    		:= advenoh
# APP    		:= rasberrypi
TAG         := app-quotes-client
IMAGE       := $(REGISTRY)/$(APP):$(TAG)

# rasberry
#	--platform linux/amd64,linux/arm/v7,linux/arm/v6
.PHONY: docker-push-rasberrypi
docker-push-rasberrypi:
	@docker buildx build \
	--platform linux/arm/v7 \
	-t $(IMAGE) --push -f Dockerfile .

.PHONY: docker-build
docker-build:
	@docker build -t $(IMAGE) -f Dockerfile .

.PHONY: docker-push
docker-push: docker-build
	@docker push $(IMAGE)