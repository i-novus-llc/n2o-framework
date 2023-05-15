import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

import HelpPopover from '../../fields/StandardField/HelpPopover'
import { withFieldsetHeader } from '../withFieldsetHeader'
import DefaultFieldset from '../DefaultFieldset'
import { Badge } from '../../../../snippets/Badge/Badge'

function TitleFieldset({
    render,
    rows,
    label,
    showLine,
    className,
    subTitle,
    help,
    disabled,
    badge,
}) {
    return (
        <DefaultFieldset disabled={disabled} className="title-fieldset">
            <div className={classNames('title-fieldset-header', { [className]: className })}>
                <Badge {...badge} visible={!!badge}>
                    {label && <span className="title-fieldset-text">{label}</span>}
                </Badge>
                <HelpPopover help={help} />
                {subTitle && <small className="text-muted title-fieldset-subtitle">{subTitle}</small>}
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
    label: PropTypes.string,
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
    badge: PropTypes.object,
}

TitleFieldset.defaultProps = {
    showLine: true,
    disabled: false,
}

export default withFieldsetHeader(TitleFieldset)
