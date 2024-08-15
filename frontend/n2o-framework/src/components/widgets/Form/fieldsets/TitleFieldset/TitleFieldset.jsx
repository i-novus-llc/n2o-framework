import React, { useContext } from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

import HelpPopover from '../../fields/StandardField/HelpPopover'
import { withFieldsetHeader } from '../withFieldsetHeader'
import DefaultFieldset from '../DefaultFieldset'
import { FactoryContext } from '../../../../../core/factory/context'
import { FactoryLevels } from '../../../../../core/factory/factoryLevels'
import Label from '../../fields/StandardField/Label'

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
    description,
    type,
}) {
    const { getComponent } = useContext(FactoryContext)
    const FactoryBadge = getComponent('Badge', FactoryLevels.SNIPPETS)

    return (
        <DefaultFieldset disabled={disabled} className="title-fieldset">
            <div className={classNames('title-fieldset-header', { [className]: className })}>
                {FactoryBadge && (
                    <FactoryBadge {...badge} visible={!!badge}>
                        {label && <span className="title-fieldset-text">{label}</span>}
                    </FactoryBadge>
                )}
                <HelpPopover help={help} />
                <Label
                    className="n2o-fieldset__description line-description"
                    value={description}
                    visible={type === 'line' && description}
                />
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
