import React from 'react'
import PropTypes from 'prop-types'
import BootstrapLabel from 'reactstrap/lib/Label'
import cx from 'classnames'

import HelpPopover from './HelpPopover'

/**
 * Лейбел поля
 * @param {string} id - уникальный индефикатор
 * @param {string|element} value - текст/элемент лейбела
 * @param {boolean} required - поле обязательное(к лейбелу добавляется звездочка) или нет
 * @param {string} className - дополнительный класс стиля
 * @param {string} style - дополнительный dom стиль
 * @param {string} help - подсказка
 * @param {object} props - остальные пропсы
 * @example
 * <Label value='Телефон'/ required={true} />
 */

const Label = ({ id, value, required, className, style, help, ...props }) => {
    const newProps = {
        className: cx('col-form-label', className),
        style: { display: 'inline-block', ...style },
    }

    return React.isValidElement(value) ? (
        <div className="n2o-field-label">
            {React.cloneElement(value, newProps)}
            {required && value ? (
                <span className="n2o-field-label-required">*</span>
            ) : (
                ''
            )}
            {help && value ? <HelpPopover id={id} help={help} /> : null}
        </div>
    ) : (
        <BootstrapLabel className={cx('n2o-field-label', className)} style={style}>
            {value}
            {required && value ? (
                <span className="n2o-field-label-required">*</span>
            ) : null}
            {help && value ? <HelpPopover id={id} help={help} /> : null}
        </BootstrapLabel>
    )
}

Label.propTypes = {
    value: PropTypes.oneOfType([PropTypes.string, PropTypes.element]),
    required: PropTypes.bool,
    className: PropTypes.string,
    style: PropTypes.object,
}

Label.defaultProps = {
    className: '',
    style: {},
}

export default Label
