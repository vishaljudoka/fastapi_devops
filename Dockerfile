FROM python:3.10

WORKDIR /vksh-api
COPY requirements.txt .
COPY ./src  ./src

EXPOSE 8000
RUN pip install -r requirements.txt

CMD ["python", "./src/main.py"]