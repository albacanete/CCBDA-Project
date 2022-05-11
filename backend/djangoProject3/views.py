from django.http import HttpResponse
from django.shortcuts import render
#from .models import Leads
from collections import Counter

# Create your views here.
def login(request):
    if request.method == 'GET':
        return render(request, 'login.html')
    elif request.method == 'POST':
        pass

def register(request):
    if request.method == 'GET':
        return render(request, 'register.html')
    elif request.method == 'POST':
        pass
