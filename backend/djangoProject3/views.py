from django.http import HttpResponse
from django.shortcuts import render, redirect
# from .models import Leads
from collections import Counter
from django.core.exceptions import ValidationError
from validate_email import validate_email
from django.contrib.auth.models import User

from django.views.decorators.csrf import csrf_exempt


from .models import *

def page_not_found_view(request, exception):
    return render(request, 'pages-error-404.html', status=404)


@csrf_exempt
def request(request):
    if request.method == 'GET':
        if "email" in request.session:
            return render(request, 'request.html', {'request_nav': True})
        else:
            return redirect('/login')
    else:
        if request.POST.get("create_model"):
            items = []
            target = request.POST.get('target')
            championship = request.POST.get('championship')
            year = request.POST.get('year')
            squad = request.POST.get('squad')
            player = request.POST.get('player')
            objects = Player_Status.objects.filter(namePlayer=player).values_list('year', 'valuePlayer')

            for object in objects:
                items.append(object)

            items = sorted(items)

            # CLEAN data
            items = [i for i in items if i[1] != -1]
            years = []
            values = []


            for i in items:
                years.append(i[0])
                values.append(i[1])

            user = User.objects.get(email=request.session['email'])
            field_object = User._meta.get_field('id')
            user_id = field_object.value_from_object(user)

            request_player = Request(target=target, nameLeague=championship, year=year, nameTeam=squad, namePlayer=player, user_id=user_id)
            request_player.save()

            return render(request, 'request.html', {'request_nav': True, 'years': years, 'values': values, 'name': player})
        else:
            items = []
            # PARAMETERS OF THE SELECTION
            if not request.POST.get('value_squad'):
                championship = request.POST.get('value_championship')
                year = request.POST.get('value_year')
                objects = Team_Status.objects.filter(nameLeague=championship, year=year).values_list('nameTeam', flat=True)
                objects = sorted(objects)
                for object in objects:
                    items.append(object+"/")

            elif not request.POST.get('value_player'):
                championship = request.POST.get('value_championship')
                year = request.POST.get('value_year')
                squad = request.POST.get('value_squad')
                objects = Player_Status.objects.filter(nameLeague=championship, year=year, nameTeam=squad).values_list('namePlayer', flat=True)

                objects = sorted(objects)
                for object in objects:
                    items.append(object + "/")
            else:
                championship = request.POST.get('value_championship')
                year = request.POST.get('value_year')
                squad = request.POST.get('value_squad')
                player = request.POST.get('value_player')
                objects = Player_Status.objects.filter(namePlayer=player).values_list('year', 'valuePlayer', flat=True)

                objects = sorted(objects)
                for object in objects:
                    items.append(object + "/")

            return HttpResponse(items)


def history(request):
    if request.method == "GET":
        if "email" in request.session:
            requests = []
            user = User.objects.get(email=request.session['email'])
            field_object = User._meta.get_field('id')
            user_id = field_object.value_from_object(user)
            try:
                requests_objects = Request.objects.filter(user_id=user_id).values_list('target', 'nameLeague', 'year', 'nameTeam', 'namePlayer', 'created_at')
                for req in requests_objects:
                    requests.append(req)

                return render(request, 'history.html', {'history_nav': True, 'requests': requests})
            except:
                return render(request, 'history.html', {'history_nav': True})
        else:
            return redirect('/login')

def user_profile(request):
    if "email" in request.session:
        if request.method == 'POST':
            user = User()
            errors = []
            email = request.session.get('email')
            password = request.POST.get('password')
            new_password = request.POST.get('new_password')
            repeated_new_password = request.POST.get('repeated_new_password')

            if user.login(email, password):
                if new_password != repeated_new_password:
                    errors.append("The password repeated is not the same")
            else:
                errors.append("Current password wrong!")

            if not errors:
                #status = user.changedPassword(email, new_password)
                return render(request, 'users-profile.html', {'success': "Password changed!"})
            else:
                return render(request, 'users-profile.html', {'errors': errors})
        else:
            return render(request, 'users-profile.html')
    else:
        return redirect('/login')

def home(request):
    if "email" in request.session:
        return render(request, 'home.html', {'home_nav': True})
    else:
        return redirect('/login')


def login(request):
    if request.method == 'GET':
        if "email" in request.session:
            return redirect('/')
        else:
            return render(request, 'login.html')
    elif request.method == 'POST':
        errors = []

        email = request.POST.get('email')
        password = request.POST.get('password')

        if not validate_email(email):
            errors.append("Bad email format")

        if not errors:
            user = User.objects.get(email=email)
            field_object = User._meta.get_field('password')
            password_server = field_object.value_from_object(user)
            password_hashed = password.encode('utf-8')

            if bcrypt.checkpw(password_hashed, password_server.encode('utf-8')):
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
            password_hashed = password.encode('utf-8')
            # Generate salt
            mySalt = bcrypt.gensalt()

            # Hash password
            hash = bcrypt.hashpw(password_hashed, mySalt)
            try:
                user = User(email=email, password=hash.decode('utf-8'))
                user.save()
                return render(request, 'register.html', {'success': "User registered!"})
            except:
                errors.append("Something wrong, user already registered?")
                return render(request, 'register.html', {'errors': errors})
        else:
            return render(request, 'register.html', {'errors': errors})


def logout(request):
    if "email" in request.session:
        del request.session["email"]
        return redirect('/login')
