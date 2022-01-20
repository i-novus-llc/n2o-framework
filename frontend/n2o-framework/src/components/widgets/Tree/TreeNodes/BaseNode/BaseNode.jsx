import React from 'react'
import PropTypes from 'prop-types'
import has from 'lodash/has'
import { Badge } from 'reactstrap'
// components

// fns
import { splitSearchText } from '../../until'

function BaseNode({
    prefixCls,
    imageFieldId,
    labelFieldId,
    badgeFieldId,
    valueFieldId,
    badgeColorFieldId,
    searchValue,
    searchKeys,
    data,
    filter,
}) {
    return (
        <span
            data-id={data[valueFieldId]}
            className={`${prefixCls}-content-wrapper cls-${data[valueFieldId]}`}
            tabIndex="-1"
        >
            {[
                has(data, imageFieldId) && (
                    <div className={`${prefixCls}-image-tree-wrapper`}>
                        <img
                            alt="not found"
                            key={`tree_img_${data[valueFieldId]}`}
                            src={data[imageFieldId]}
                        />
                    </div>
                ),
                has(data, labelFieldId) && (
                    <span
                        key={`tree_label_${data[valueFieldId]}`}
                        className={`${prefixCls}-label`}
                    >
                        {searchKeys.includes(data[valueFieldId]) && searchValue
                            ? splitSearchText(data[labelFieldId], searchValue, filter)
                            : data[labelFieldId]}
                    </span>
                ),
                has(data, badgeFieldId) && (
                    <Badge
                        key={`tree_badge_${data[valueFieldId]}`}
                        color={data[badgeColorFieldId]}
                    >
                        {data[badgeFieldId]}
                    </Badge>
                ),
            ]}
        </span>
    )
}

BaseNode.propTypes = {
    prefixCls: PropTypes.string,
    imageFieldId: PropTypes.string,
    labelFieldId: PropTypes.string,
    badgeFieldId: PropTypes.string,
    valueFieldId: PropTypes.string,
    badgeColorFieldId: PropTypes.string,
    searchValue: PropTypes.string,
    searchKeys: PropTypes.string,
    data: PropTypes.object,
    filter: PropTypes.any,
}

export default BaseNode
