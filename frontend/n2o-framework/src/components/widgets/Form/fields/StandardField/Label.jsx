import React from 'react'
import PropTypes from 'prop-types'
import { Label as BootstrapLabel } from 'reactstrap'
import classNames from 'classnames'

import HelpPopover from './HelpPopover'
import { Required } from './Required'

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

const Label = ({ id, value, required, className, style, help }) => {
    if (!value) {
        return null
    }

    const newProps = {
        className: classNames('col-form-label', className),
        style: { display: 'inline-block', ...style },
    }

    if (React.isValidElement(value)) {
        return (
            <div className="n2o-field-label">
                {React.cloneElement(value, newProps)}
                <Required required={required} />
                <HelpPopover id={id} help={help} />
            </div>
        )
    }

    return (
        <BootstrapLabel className={classNames('n2o-field-label', className)}>
            <section style={style}>
                <span>{value}</span>
                <Required required={required} />
            </section>
            <HelpPopover id={id} help={help} />
        </BootstrapLabel>
    )
}

Label.propTypes = {
    value: PropTypes.oneOfType([PropTypes.string, PropTypes.element]),
    required: PropTypes.bool,
    className: PropTypes.string,
    id: PropTypes.string,
    help: PropTypes.string,
    style: PropTypes.object,
}

Label.defaultProps = {
    className: '',
    style: {},
}

export default Label
