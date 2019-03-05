import React from 'react';
import { has, uniqueId } from 'lodash';
import { Badge } from 'reactstrap';
import Icon from '../../../../snippets/Icon/Icon';
//components

//fns
import { splitSearchText } from '../../until';

function BaseNode({
  prefixCls,
  iconFieldId,
  imageFieldId,
  labelFieldId,
  badgeFieldId,
  valueFieldId,
  badgeColorFieldId,
  searchValue,
  searchKeys,
  data
}) {
  console.log(searchValue, searchKeys, searchKeys.includes(data[valueFieldId]));

  return (
    <span
      data-id={data[valueFieldId]}
      className={`${prefixCls}-content-wrapper cls-${data[valueFieldId]}`}
      tabIndex="-1"
    >
      {[
        has(data, iconFieldId) && <Icon key={uniqueId('tree_icon_')} name={data[iconFieldId]} />,
        has(data, imageFieldId) && (
          <div className={`${prefixCls}-image-tree-wrapper`}>
            <img alt="not found" key={uniqueId('tree_img_')} src={data[imageFieldId]} />
          </div>
        ),
        has(data, labelFieldId) && (
          <span key={uniqueId('tree_label_')} className={`${prefixCls}-label`}>
            {searchKeys.includes(data[valueFieldId]) && searchValue
              ? splitSearchText(data[labelFieldId], searchValue)
              : data[labelFieldId]}
          </span>
        ),
        has(data, badgeFieldId) && (
          <Badge key={uniqueId('tree_badge_')} color={data[badgeColorFieldId]}>
            {data[badgeFieldId]}
          </Badge>
        )
      ]}
    </span>
  );
}

export default BaseNode;
