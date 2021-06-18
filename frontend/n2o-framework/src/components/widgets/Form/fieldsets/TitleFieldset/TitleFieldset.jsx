import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

function TitleFieldset({ render, rows, title, showLine, className, subTitle }) {
    return (
        <div className="title-fieldset">
            <div className={classNames('title-fieldset-header', { [className]: className })}>
                {title && <span className="title-fieldset-text">{title}</span>}
                {subTitle && (
                    <small className="text-muted title-fieldset-subtitle">
                        {subTitle}
                    </small>
                )}
                {showLine && <div className="title-fieldset-line" />}
            </div>
            {render(rows)}
        </div>
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
}

TitleFieldset.defaultProps = {
    showLine: true,
}

export default TitleFieldset
