from django.http import HttpResponse
from django.shortcuts import render, redirect
#from .models import Leads
from collections import Counter
from .models import *


def home(request):
    if "email" in request.session:
        return render(request, 'home.html')
    else:
        return redirect('/login')

# Create your views here.
def login(request):
    if request.method == 'GET':
        """if "email" in request.session:
            return redirect('/home')
        else:"""
        return render(request, 'login.html')
    elif request.method == 'POST':
        email = request.POST.get('email')
        password = request.POST.get('password')
        print(email)

        if login_model(email, password):
            request.session["email"] = str(email)
        return redirect('/home')

def register(request):

    if request.method == 'GET':
        return render(request, 'register.html')
    elif request.method == 'POST':
        email = request.POST.get('email')
        password = request.POST.get('password')
        repeat_password = request.POST.get('repeat_password')

        if password == repeat_password:
            register_model(email, password)

        return render(request, 'register.html')

