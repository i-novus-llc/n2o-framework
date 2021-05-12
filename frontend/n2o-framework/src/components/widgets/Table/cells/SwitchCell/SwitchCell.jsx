import React from 'react'
import PropTypes from 'prop-types'
import get from 'lodash/get'

import Factory from '../../../../../core/factory/Factory'
import { CELLS } from '../../../../../core/factory/factoryLevels'

function SwitchCell({
    model,
    switchFieldId,
    switchList,
    switchDefault,
    ...props
}) {
    const currentCellType = model[switchFieldId]
    const cellProps = get(switchList, currentCellType, switchDefault)

    return <Factory level={CELLS} model={model} {...props} {...cellProps} />
}

SwitchCell.propTypes = {
    /**
   * props: метаданные,
   * из которых по switchFieldId
   * в switchList берется Cell.
   * Если ключ не подходит,
   * Cell по switchDefault
   * */
    props: PropTypes.object,
}

export default SwitchCell
