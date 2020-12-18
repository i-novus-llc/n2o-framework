import React from 'react';
import map from 'lodash/map';
import classNames from 'classnames';

import Factory from '../../../../../core/factory/Factory';
import { SNIPPETS } from '../../../../../core/factory/factoryLevels';

export default function ImageStatuses({ statuses, className }) {
  return (
    <div className={classNames('n2o-image-statuses', className)}>
      {map(statuses, (status, index) => {
        const {
          src,
          statusFieldId,
          statusIconFieldId,
          statusPlace,
          iconDirection,
        } = status;

        const props = {
          text: statusFieldId,
          icon: statusIconFieldId,
          place: statusPlace,
          iconDirection: iconDirection,
        };

        return (
          <Factory
            level={SNIPPETS}
            key={index}
            className={statusPlace}
            src={src}
            {...props}
          />
        );
      })}
    </div>
  );
}
