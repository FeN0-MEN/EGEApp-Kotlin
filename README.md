# EGEApp

EGEApp — это Android-приложение для подготовки к экзаменам ЕГЭ. Приложение позволяет пользователям создавать свои тестировочные задания, проходить симуляцию ЕГЭ, просматривать теоретические материалы для экзамена и отслеживать свою статистику по времени и баллам за тесты, а также визуализировать данные в виде графиков. Также для мобильного приложения создан веб-интерфейс, который позволяет администратору добвлять новые и редактировать старые задания в приложении.

## Основные функции

- Регистрация и авторизация пользователей с использованием Firebase Authentication и сервиса Google.
- Хранение и получение статистики пользователей по времени и баллам с использованием Firebase Realtime Database.
- Визуализация статистики с помощью графиков.
- Добавление и изменение заданий с помощью веб-интерфейса на Django Framework.

## Связанные проекты

- [EGEApp Web Interface](https://github.com/FeN0-MEN/WebApp): Веб-интерфейс для управления заданиями, используемыми в этом приложении.

## Установка

### Необходимые условия

- Android Studio 5.0 или выше
- Установленный Android SDK

### Инструкции

1. Клонируйте репозиторий:
    ```sh
    git clone https://github.com/username/EGEApp.git
    ```

2. Откройте проект в Android Studio.

3. Добавьте файл `google-services.json` в каталог `app/`. Файл можно получить из Firebase Console, следуя [этой инструкции](https://firebase.google.com/docs/android/setup#manually_add_firebase).

4. Синхронизируйте проект с Gradle.

5. Запустите проект на эмуляторе или физическом устройстве.

## Использование

1. Зарегистрируйтесь или войдите в приложение с помощью Google.
2. Пройдите тесты и отслеживайте свою статистику по времени и баллам.
3. Визуализируйте свою статистику на графиках.

## Скриншоты

![Главный экран](https://drive.google.com/file/d/1Ny0XhRfKpE9cBNjV4OD4thNA5yY_v7yI/view?usp=sharing)
![Статистика](https://drive.google.com/file/d/1O8SDcTk6PEQIQ0m6zeIrr87WhrAZRRt0/view?usp=sharing)
![Экзамен](https://drive.google.com/file/d/1Nyt2gwLXYll4v30nC5bsRA48z8C4i_4e/view?usp=sharing)
![Результаты экзамена](https://drive.google.com/file/d/1Q2X3unnwb527io_KFGoPbYwzBPfLjKoG/view?usp=sharing)
![Выбор заданий для теста](https://drive.google.com/file/d/1Nyt2gwLXYll4v30nC5bsRA48z8C4i_4e/view?usp=sharing)
![Созданный тест](https://drive.google.com/file/d/1O2WpXcqB8vIR9-t2wJwVutqmanYh3OSQ/view?usp=sharing)
![Подсказка для задания](https://drive.google.com/file/d/1O4jyvZSojRDA0cyYERAbEHBm-wMQkqiR/view?usp=sharing)
![Результаты созданного теста](https://drive.google.com/file/d/1NzXHiinqhs-0n3exptMHz3ejr5t6yYoP/view?usp=sharing)
![Выбор теории](https://drive.google.com/file/d/1Q5eoBJVGXGkVeMD2zDBbSLf69Gtrh8Na/view?usp=sharing)
![Просмотр теории](https://drive.google.com/file/d/1Q4TA0hfYTS5MJg-vI0KwP3ONuLPEA-4c/view?usp=sharing)
![Информация об экзамене](https://drive.google.com/file/d/1Q9KgBChyT9b47YVll5iCjMYjME6lj-a4/view?usp=sharing)
![Авторизация](https://drive.google.com/file/d/1Q-EOoIM9h4gic9ksvZ2EfI8ItVkcCnR1/view?usp=sharing)
![Регистрация](https://drive.google.com/file/d/1Q-JJDsHhlF1QCDUuAMWxgcOvDOXwyXim/view?usp=sharing)



## Технологии

- Kotlin
- Firebase Authentication
- Firebase Realtime Database
- MPAndroidChart

## Контрибьюция

Мы приветствуем вклад в развитие проекта! Если вы хотите внести изменения, пожалуйста, следуйте этим шагам:

1. Сделайте форк репозитория.
2. Создайте новую ветку для своих изменений:
    ```sh
    git checkout -b feature/YourFeature
    ```
3. Внесите необходимые изменения и закоммитьте их:
    ```sh
    git commit -m 'Add some feature'
    ```
4. Запушьте изменения в ветку:
    ```sh
    git push origin feature/YourFeature
    ```
5. Создайте Pull Request.

## Лицензия

Этот проект лицензирован под лицензией MIT. Подробнее см. в файле [LICENSE](LICENSE).

## Контакты

Если у вас есть вопросы или предложения, вы можете связаться с нами по адресу: nmishunin@tennisi.it
