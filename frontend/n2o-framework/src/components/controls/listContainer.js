import React from 'react'
import { compose } from 'redux'

import withFetchData from './withFetchData'
import withListContainer from './withListContainer'

/**
 * Композер хоков {@Link withFetchData} & {@Link withListContainer}
 * @param WrappedComponent - оборачиваемый компонент
 */

const listContainer = WrappedComponent => compose(
    withFetchData,
    withListContainer,
)(WrappedComponent)

export default listContainer
