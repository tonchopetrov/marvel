
kubectl apply -f zoo.yml
sleep 7
kubectl apply -f kafka.yml
kubectl apply -f db.yml
sleep 5
kubectl apply -f cdc-service.yml
sleep 10
kubectl apply -f kafka-ui.yml