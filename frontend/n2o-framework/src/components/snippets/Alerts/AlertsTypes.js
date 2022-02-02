import PropTypes from 'prop-types'

export const AlertTypes = {
    /**
     * Заголовок алерта
     */
    title: PropTypes.string,
    /**
     * Текст алерта
     */
    text: PropTypes.string,
    /**
     * Цвет алерта
     */
    color: PropTypes.oneOf(['info', 'danger', 'warning', 'success']),
    /**
     * Подробности алерта
     */
    details: PropTypes.string,
    /**
     * Флаг показа кнопки закрытия
     */
    closeButton: PropTypes.bool,
    /**
     * Callback на закрытие
     */
    onDismiss: PropTypes.func,
    /**
     * Класс алерта
     */
    className: PropTypes.string,
    /**
     * Стили
     */
    style: PropTypes.object,
    /**
     * Иконка рядом с заголовком
     */
    icon: PropTypes.string,
    /**
     * Видимость
     */
    visible: PropTypes.bool,
    /**
     * Позиционирование алерта
     */
    // eslint-disable-next-line react/no-unused-prop-types
    position: PropTypes.string,
    /**
     * Кнопка tooltip
     */
    help: PropTypes.string,
    /**
     * Флаг включения всплытия с анимацией
     */
    animate: PropTypes.bool,
    t: PropTypes.func,
    loader: PropTypes.any,
    /**
     * переход -> href по клику на alert
     */
    href: PropTypes.string,
    /**
     * временная точка отправки уведомления на клиент
     */
    timestamp: PropTypes.number,
}
