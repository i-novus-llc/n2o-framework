import React from 'react'
import classNames from 'classnames'
import map from 'lodash/map'
import { withResizeDetector } from 'react-resize-detector'

import { withWidgetHandlers } from '../hocs/withWidgetHandlers'

import { calcCols } from './utils'
import { TilesCell } from './TilesCell'
import { type TilesType, type TilesModel } from './types'

/**
 * Tiles
 * @reactProps {string} className - имя css класса карточки
 * @reactProps {array} tiles - массив объектов cell из которых состоит виджет
 * @reactProps {array} data - данные объектов cell
 * @reactProps {number} colsSm - количество колонок в мобильном режиме
 * @reactProps {number} colsMd - количество колонок в режиме планшета
 * @reactProps {number} colsLg - количество колонок в режиме десктопа
 * @reactProps {number} id - id виджета
 * @reactProps {string} tileWidth - ширина ввиджета
 * @reactProps {string} tileHeight - высота виджета
 * @reactProps {string} datasource - datasource key
 */

function TilesBody({
    tile,
    className,
    data,
    widgetId,
    colsSm,
    colsMd,
    colsLg,
    width,
    onResolve,
    dispatch,
    datasource,
    tileWidth = '260px',
    tileHeight = '350px',
}: TilesType) {
    const col = calcCols(colsSm, colsMd, colsLg, width)

    const renderTilesItem = (model: TilesModel, index: number) => (
        <div className={`col-${col} d-flex justify-content-center`}>
            <div className={classNames('n2o-tiles__item')} style={{ width: tileWidth, minHeight: tileHeight }}>
                {tile.map(cell => (
                    <TilesCell
                        index={index}
                        widgetId={widgetId}
                        model={model}
                        onResolve={onResolve}
                        dispatch={dispatch}
                        datasource={datasource}
                        {...cell}
                    />
                ))}
            </div>
        </div>
    )

    return (
        <div className={classNames('n2o-tiles__container col-12', className)}>
            {data?.length ? map(data, (model, index) => renderTilesItem(model, index)) : ''}
        </div>
    )
}

export const Tiles = withWidgetHandlers(
    withResizeDetector(TilesBody),
)
export default Tiles
