import PropTypes from 'prop-types'

export const propTypes = {
    /**
   * Флаг отображения в виде кода
   */
    code: PropTypes.bool,

    del: PropTypes.bool,
    /**
   * Марк
   */
    mark: PropTypes.bool,
    /**
   * Жирный текст
   */
    strong: PropTypes.bool,
    /**
   * Подчеркивание текста
   */
    underline: PropTypes.bool,
    /**
   * Флаг отображения текста маленьким
   */
    small: PropTypes.bool,
    /**
   * Текст
   */
    text: PropTypes.string,
    children: PropTypes.node,
    /**
   * Callback на изменение
   */
    onChange: PropTypes.func,
    /**
   * Цвет
   */
    color: PropTypes.string,
}

export const defaultProps = {
    code: false,
    del: false,
    mark: false,
    strong: false,
    underline: false,
    small: false,
    text: '',
    onChange: () => {},
    color: '',
}
