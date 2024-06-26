# Экраны "Крокодил"

## Регистрация
На экране регистрации пользователь вводит желаемый логин и пароль. Если логин занят, то пользователю отображается сообщение об этом.
При успешной регистрации пользователя переадресовывает на страницу авторизации.

![регистрация](images/login_taken.png)

## Авторизация
На экране авторизации пользователь вводит свой логин и пароль. Если аккаунта не существует или указан неверный пароль, пользователь получает соответствующее сообщение об ошибке.
При успешной авторизации пользователя переадресовывает на страницу списка комнат.

![авторизация](images/wrong_pass.png)

## Список комнат
На странице списка комнат отображается список всех активных комнат.
В каждую из комнат можно зайти.
Так же пользователь может создать свою комнату, нажав кнопку "Create room". Появится всплывающее окно создания комнаты.

Также пользователь может попасть в комнату по прямой ссылке. 
Таким образом можно пригласить друга сразу в комнату.

![список комнат](images/room_list.png)

## Создание комнаты
На экране создания комнаты пользователь может ввести желаемое название комнаты.
Если такая комната уже существует, ему отображается сообщение об этом.
При успешном создании комнаты пользователя переадресовывает на страницу комнаты.

![создание комнаты](images/room_name_taken.png)

## Комната
Попадая в комнату пользователь становится рисующим или угадывающим.
В каждой комнате всего один рисующий. Если рисующий выходит из игры, новый рисующий выбирается случайным образом.
Если из комнаты вышли все пользователи, комната удаляется.

Рисующему дается на выбор три слова.

![выбор слова](images/choose_word.png)

Послы выбора слова рисующий приступает к его рисованию.
Он может:
- выбрать цвет из уже заготовленных, или указать его на палитре
- выбрать размер кисти
- очистить холст
  Так же рисующий может видеть чат и лайкать или дизлайкать слова, подсказывая таким образом угадывающим.

![интерфейс рисующего слова](images/room_draw.png)

Угадывающий может писать слова в чат, а также видит всё, что рисует рисующий, и его реакции на слова в чате.

![интерфейс угадывающего](images/room_guess.png)

Когда угадывающий пишет правильное слово в чат, он становится рисующим.

## Друзья
На этом экране можно увидеть список друзей и список всех входящих/исходящих запросов в друзья.

В списке друзей отображается, в какой комнате сейчас играют друзья. Нажав на комнату, можно зайти в неё.

![в игру из списка друзей](images/go_to_game.png)

В списке входящих запросов отображается имя пользователя, а также кнопки, нажатие на которые принимает или отклоняет запрос.
В списке исходящих запросов отображается имя пользователя, а также кнопка, нажатие на которую отменяет запрос.

![в игру из списка друзей](images/incoming.png)

Нажатие на кнопку "Add friend" открывает всплывающее окно, 
в котором можно ввести текст и увидеть список пользователей, имя которых начинается с этого текста.
Рядом с именем пользователя отображаются значки "отправить запрос", "отменить запрос" и "уже в друзьях".
![в игру из списка друзей](images/find_user.png)

![в игру из списка друзей](images/already_friend.png)
