OS := $(shell uname)

preview: 
	echo $(PREVIEW_VERSION)
	echo $(DOCKER_REGISTRY)
ifeq ($(OS),Darwin)
	sed -i "" -e "s/version:.*/version: $(PREVIEW_VERSION)/" Chart.yaml
	sed -i "" -e "s/version:.*/version: $(PREVIEW_VERSION)/" ../*/Chart.yaml
	sed -i "" -e "s/tag: .*/tag: $(PREVIEW_VERSION)/" values.yaml
else ifeq ($(OS),Linux)
	sed -i -e "s/version:.*/version: $(PREVIEW_VERSION)/" Chart.yaml
	sed -i -e "s/version:.*/version: $(PREVIEW_VERSION)/" ../*/Chart.yaml
	sed -i -e "s|repository: .*|repository: $(DOCKER_REGISTRY)\/cicdday\/micro|" values.yaml
	sed -i -e "s/tag: .*/tag: $(PREVIEW_VERSION)/" values.yaml
else
	echo "platfrom $(OS) not supported to release from"
	exit -1
endif
	echo "  version: $(PREVIEW_VERSION)" >> requirements.yaml
	echo "requirements"
	cat requirements.yaml
	echo "values"
	cat values.yaml
	echo "requirements"
	cat ../micro/requirements.yaml
	echo "values"
	cat ../micro/values.yaml
	echo "chart"
	cat ../micro/Chart.yaml
	jx step helm build
	