from django.http import HttpResponse
from django.shortcuts import render, redirect
#from .models import Leads
from collections import Counter
from django.core.exceptions import ValidationError
from validate_email import validate_email

from .models import *

def home(request):
    if "email" in request.session:
        return render(request, 'home.html')
    else:
        return redirect('/login')

def login(request):
    errors = []
    if request.method == 'GET':
        if "email" in request.session:
            return redirect('/')
        else:
            return render(request, 'login.html')
    elif request.method == 'POST':
        email = request.POST.get('email')
        password = request.POST.get('password')

        if not validate_email(email):
            errors.append("Bad email format")

        if login_model(email, password):
            request.session["email"] = str(email)
            return redirect('/')
        else:
            errors.append("Email or Password wrong!")
            return render(request, 'login.html', {'errors': errors})

def register(request):
    errors = []
    if request.method == 'GET':
        return render(request, 'register.html')
    elif request.method == 'POST':

        email = request.POST.get('email')
        password = request.POST.get('password')
        repeat_password = request.POST.get('repeat_password')

        if not validate_email(email):
            errors.append("Bad email format")

        if password != repeat_password:
            errors.append("The password repeated is not the same as the original one")

        if not errors:
            status = register_model(email, password)
            if not status:
                return render(request, 'register.html', {'success': "User registered!"})
            else:
                for error in status:
                    errors.append(error)
                return render(request, 'register.html', {'errors': errors})
        else:
            return render(request, 'register.html', {'errors': errors})



def logout(request):
    if "email" in request.session:
        del request.session["email"]
        return redirect('/login')

