import React from 'react'
import classNames from 'classnames'
import map from 'lodash/map'
import { withResizeDetector } from 'react-resize-detector'

import calcCols from './utils'
import TilesCell from './TilesCell'

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

function Tiles({
    tile,
    className,
    data,
    id,
    colsSm,
    colsMd,
    colsLg,
    width,
    tileWidth,
    tileHeight,
    onResolve,
    dispatch,
    datasource,
}) {
    const col = calcCols(colsSm, colsMd, colsLg, width)

    const renderTilesItem = (element, index) => (
        <div className={`col-${col} d-flex justify-content-center`}>
            <div
                className={classNames('n2o-tiles__item')}
                style={{ width: tileWidth, minHeight: tileHeight }}
            >
                {map(tile, cell => (
                    <TilesCell
                        index={index}
                        widgetId={id}
                        model={element}
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
            {
                data?.length
                    ? map(data, (element, index) => renderTilesItem(element, index))
                    : ''
            }
        </div>
    )
}

Tiles.defaultProps = {
    tileWidth: '260px',
    tileHeight: '350px',
}

export { Tiles }

export default withResizeDetector(Tiles)
