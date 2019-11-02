
wait-for-resource:
	./wait-for-app.sh $(RESOURCE_URL) 200

test:
	./test.sh $(RESOURCE_URL)

it-test:
	mvn failsafe:integration-test failsafe:verify -Dtest-target=${PREVIEW_URL} -f ../pom.xml

