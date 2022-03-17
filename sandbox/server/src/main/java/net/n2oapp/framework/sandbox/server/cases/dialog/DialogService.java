package net.n2oapp.framework.sandbox.server.cases.dialog;

import org.springframework.stereotype.Service;

import javax.persistence.NonUniqueResultException;

@Service
public class DialogService {
    /**
     * Зарегистрировать нового пользователя
     * @param name Имя пользователя
     * @param useExists Использовать существующего в случае дубля?
     * @return Сообщение об успешном сохранении
     * @throws NonUniqueResultException Пользователь с таким именем уже существует
     */

    public String register(String name, Boolean useExists) {
        if (name.equalsIgnoreCase("Joe") && !Boolean.FALSE.equals(useExists)) {
            if (useExists != null)
                return name + " объединен с существующим";
            else
                throw new NonUniqueResultException("Пользователь с таким именем уже существует");
        } else {
            return name + " зарегистрирован новый";
        }
    }

}
