import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

import HelpPopover from '../../fields/StandardField/HelpPopover'
import { withFieldsetHeader } from '../withFieldsetHeader'
import DefaultFieldset from '../DefaultFieldset'

function TitleFieldset({ render, rows, title, showLine, className, subTitle, help, disabled }) {
    return (
        <DefaultFieldset disabled={disabled} className="title-fieldset">
            <div className={classNames('title-fieldset-header', { [className]: className })}>
                {title && <span className="title-fieldset-text">{title}</span>}
                {help && <HelpPopover help={help} />}
                {subTitle && (
                    <small className="text-muted title-fieldset-subtitle">
                        {subTitle}
                    </small>
                )}
                {showLine && <div className="title-fieldset-line" />}
            </div>
            {render(rows)}
        </DefaultFieldset>
    )
}

TitleFieldset.propTypes = {
    /**
     * Функция рендера
     */
    render: PropTypes.func,
    /**
     * Масив строк для реднра
     */
    rows: PropTypes.array,
    /**
     * Заголовок
     */
    title: PropTypes.string,
    /**
     * Флаг включения линии рядом с заголовком
     */
    showLine: PropTypes.bool,
    /**
     * Класс
     */
    className: PropTypes.string,
    /**
     * Текст подзаголовка
     */
    subTitle: PropTypes.string,
    /**
     * Подсказка около title
     */
    help: PropTypes.string,
    disabled: PropTypes.bool,
}

TitleFieldset.defaultProps = {
    showLine: true,
    disabled: false,
}

export default withFieldsetHeader(TitleFieldset)
