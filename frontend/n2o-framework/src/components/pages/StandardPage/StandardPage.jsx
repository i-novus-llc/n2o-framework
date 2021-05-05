import React from 'react'
import PropTypes from 'prop-types'

import DefaultPage from '../DefaultPage'
import PageRegions from '../PageRegions'

/**
 * Страница с одной зоной
 * @param id
 * @param regions
 * @param rest
 * @return {*}
 * @constructor
 */
function StandardPage({ id, regions, ...rest }) {
    return (
        <DefaultPage {...rest}>
            <div className="n2o-page n2o-page__single-layout">
                <PageRegions id={id} regions={regions} />
            </div>
        </DefaultPage>
    )
}

StandardPage.propTypes = {
    id: PropTypes.string,
    regions: PropTypes.object,
}

StandardPage.defaultProps = {
    regions: {},
}

export default StandardPage
