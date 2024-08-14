import React, { useLayoutEffect } from 'react'
import { connect, useDispatch } from 'react-redux'
import PropTypes from 'prop-types'
import { compose, setDisplayName } from 'recompose'
import classNames from 'classnames'
import pick from 'lodash/pick'
import isEmpty from 'lodash/isEmpty'

import { Panel, Collapse } from '../../snippets/Collapse/Collapse'
import withWidgetProps from '../withWidgetProps'
import { RegionContent } from '../RegionContent'
import { registerRegion, unregisterRegion } from '../../../ducks/regions/store'

/**
 * Регион Лист
 * @reactProps {array} content - массив из объектов, которые описывают виджет{id, name, opened, pageId, widget}
 * @reactProps {bool} expand - флаг открыт ли при загрузке (default = true)
 * @reactProps {bool} hasSeparator - есть ли разделительная линия (default = true)
 * @reactProps {string} pageId - идентификатор страницы
 * @reactProps {bool} forceRender - Флаг отключения ленивого рендера
 */

function ListRegion(props) {
    const { id: regionId, parent, collapsible, getWidgetProps, className, style, disabled, expand,
        isVisible, hasSeparator, label, pageId, regionsState, content = [] } = props
    const dispatch = useDispatch()

    useLayoutEffect(() => {
        dispatch(registerRegion(regionId, {
            regionId,
            isInit: true,
            parent,
            visible: true,
        }))

        return () => {
            dispatch(unregisterRegion(regionId))
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [])

    const collapseProps = pick(props, 'destroyInactivePanel', 'accordion')
    const panelProps = pick(props, [
        'type', 'forceRender', 'collapsible', 'expand', 'hasSeparator',
        'label', 'content', 'isVisible', 'pageId', 'regionsState',
    ])

    const visible = content.some((meta = {}) => {
        const { id, tabs } = meta

        if (tabs) {
            if (isEmpty(regionsState) || !regionsState[id]) { return false }

            return regionsState[id].visible
        }

        const { visible = true } = getWidgetProps(id) || {}

        return visible
    })

    return (
        <div
            className={classNames('n2o-list-region', className, { 'n2o-disabled': disabled, 'd-none': !visible })}
            style={style}
        >
            <Collapse
                defaultActiveKey="open"
                collapsible={collapsible}
                className="n2o-list-region__collapse"
                {...collapseProps}
            >
                <Panel
                    {...panelProps}
                    key={expand ? 'open' : 'close'}
                    header={<span className="n2o-list-region__collapse-name">{label}</span>}
                    className={classNames({ line: hasSeparator, 'd-none': isVisible === false })}
                >
                    <RegionContent content={content} pageId={pageId} />
                </Panel>
            </Collapse>
        </div>
    )
}

ListRegion.propTypes = {
    className: PropTypes.string,
    style: PropTypes.object,
    content: PropTypes.array.isRequired,
    pageId: PropTypes.string.isRequired,
    forceRender: PropTypes.bool,
    resolveVisibleDependency: PropTypes.func,
    collapsible: PropTypes.bool,
    isVisible: PropTypes.bool,
    disabled: PropTypes.bool,
    hasSeparator: PropTypes.bool,
    getWidgetProps: PropTypes.func,
    label: PropTypes.string,
    expand: PropTypes.bool,
}

ListRegion.defaultProps = {
    collapsible: true,
    hasSeparator: true,
    expand: true,
}

export { ListRegion }
export default compose(
    setDisplayName('ListRegion'),
    withWidgetProps,
    // widgetsState - тригер, нужен для расчета видимости виджетов в регионе
    connect(({ regions, widgets }) => ({ regionsState: regions, widgetsState: widgets })),
)(ListRegion)
