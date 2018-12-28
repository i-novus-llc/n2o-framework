import React from 'react';
import { uniqueId, has } from 'lodash';
import { Badge } from 'reactstrap';
import Icon from '../../snippets/Icon/Icon';

export const visiblePartPopup = (
  item,
  { prefixCls, iconFieldId, imageFieldId, labelFieldId, badgeFieldId, badgeColorFieldId }
) => (
  <span className={`${prefixCls}-content-wrapper`}>
    {[
      has(item, iconFieldId) && <Icon key={uniqueId('tree_icon_')} name={item[iconFieldId]} />,
      has(item, imageFieldId) && (
        <div className={`${prefixCls}-image-tree-wrapper`}>
          <img alt="not found" key={uniqueId('tree_img_')} src={item[imageFieldId]} />
        </div>
      ),
      has(item, labelFieldId) && (
        <span key={uniqueId('tree_label_')} className={`${prefixCls}-label`}>
          {item[labelFieldId]}
        </span>
      ),
      has(item, badgeFieldId) && (
        <Badge key={uniqueId('tree_badge_')} color={item[badgeColorFieldId]}>
          {item[badgeFieldId]}
        </Badge>
      )
    ]}
  </span>
);
