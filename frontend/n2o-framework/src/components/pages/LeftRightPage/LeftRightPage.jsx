import React from 'react'
import { mapProps } from 'recompose'
import get from 'lodash/get'
import PropTypes from 'prop-types'

import DefaultPage from '../DefaultPage'
import PageRegions from '../PageRegions'

/**
 * Страница с зонами слева и справа
 * @param id
 * @param metadata
 * @param regions
 * @param width
 * @param rest
 * @constructor
 */
function LeftRightPage({ id, regions, width, ...rest }) {
    return (
        <DefaultPage {...rest}>
            <div className="n2o-page n2o-page__left-right-layout">
                <PageRegions id={id} regions={regions} width={width} />
            </div>
        </DefaultPage>
    )
}

LeftRightPage.propTypes = {
    id: PropTypes.string,
    regions: PropTypes.object,
    width: PropTypes.object,
}

LeftRightPage.defaultProps = {
    width: {},
}

export default mapProps(props => ({
    ...props,
    width: get(props, 'metadata.width', {}),
}))(LeftRightPage)
