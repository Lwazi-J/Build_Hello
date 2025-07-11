# Spring Boot Kubernetes Deployment Guide

![Spring Boot + Kubernetes](https://miro.medium.com/max/1400/1*J9XJRYexqFYA8Kfm6h-lZg.png)

## Table of Contents
- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [Detailed Deployment](#detailed-deployment)
- [Accessing the Application](#accessing-the-application)
- [Troubleshooting Guide](#troubleshooting-guide)
- [Cleanup](#cleanup)

## Prerequisites

✔️ **Local Development Environment**:
- Docker installed
- Minikube v1.8+
- kubectl configured
- Java 17+ JDK
- Maven

✔️ **Project Files**:
- Spring Boot application with Dockerfile
- Kubernetes manifest files

## Quick Start

```bash
# 1. Start Minikube
minikube start

# 2. Build and load Docker image
docker build -t helloimage .
minikube image load helloimage

# 3. Deploy to Kubernetes
kubectl apply -f k8s/

# 4. Access the application
minikube service hello-app-service
```

## Detailed Deployment

### 1. Build and Load Docker Image

```bash
# Build the JAR file
./mvnw clean package

# Build Docker image
docker build -t helloimage .

# Load into Minikube
minikube image load helloimage
```

### 2. Kubernetes Manifests

**`k8s/deployment.yaml`**:
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: hello-app
spec:
  replicas: 2
  selector:
    matchLabels:
      app: hello-app
  template:
    metadata:
      labels:
        app: hello-app
    spec:
      containers:
      - name: hello-app
        image: helloimage
        imagePullPolicy: Never  # Critical for local images
        ports:
        - containerPort: 8080
        resources:
          limits:
            memory: "512Mi"
            cpu: "500m"
        livenessProbe:
          httpGet:
            path: /actuator/health
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
```

**`k8s/service.yaml`**:
```yaml
apiVersion: v1
kind: Service
metadata:
  name: hello-app-service
spec:
  selector:
    app: hello-app
  ports:
    - protocol: TCP
      port: 80
      targetPort: 8080
  type: LoadBalancer
```

### 3. Apply Configuration

```bash
# Create resources
kubectl apply -f k8s/

# Verify deployment
kubectl get all
```

## Accessing the Application

**Method 1: Minikube Service**
```bash
minikube service hello-app-service
```

**Method 2: Port Forwarding**
```bash
kubectl port-forward service/hello-app-service 8080:80
```
Access at: `http://localhost:8080`

**Method 3: Direct URL**
```bash
echo "http://$(minikube ip):$(kubectl get svc hello-app-service -o jsonpath='{.spec.ports[0].nodePort}')"
```

## Troubleshooting Guide

### 1. Image Pull Issues

**Symptoms**:
- `ErrImagePull` or `ImagePullBackOff` errors
- Pods stuck in `ContainerCreating`

**Solution**:
```bash
# Verify image exists in Minikube
minikube image ls

# Reload image if needed
minikube image load helloimage --overwrite

# Check pod details
kubectl describe pod <pod-name>
```

### 2. Application Not Reachable (404)

**Check endpoints**:
```bash
kubectl logs <pod-name> | grep "Mapped"
```

**Test endpoints**:
```bash
curl $(minikube service hello-app-service --url)/actuator/health
```

### 3. CrashLoopBackOff

**Diagnose**:
```bash
# Get crash logs
kubectl logs <pod-name> --previous

# Check resource usage
kubectl top pod <pod-name>
```

### 4. Service Unavailable

**Debug steps**:
```bash
# Verify endpoints exist
kubectl get endpoints hello-app-service

# Check service details
kubectl describe service hello-app-service
```

## Cleanup

**Remove deployment**:
```bash
kubectl delete -f k8s/
```

**Full Minikube reset**:
```bash
minikube stop
minikube delete
```

## Best Practices

1. **Image Tagging**: Always use specific tags (`:v1.0.0`) in production
2. **Health Checks**: Implement proper liveness/readiness probes
3. **Resource Limits**: Always define resource requests and limits
4. **Persistent Storage**: Use PV/PVC for database storage

## Monitoring

```bash
# Kubernetes dashboard
minikube dashboard

# View cluster events
kubectl get events --sort-by=.metadata.creationTimestamp
```

---