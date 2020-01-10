/**
 * Created by emamoshin on 22.08.2017.
 */
import React from 'react';
import PropTypes from 'prop-types';
import Place from '../Place';
import cn from 'classnames';
import layoutPlaceResolver from '../LayoutPlaceResolver';

/**
 * Стандартный Layout виджета, Places: top, topLeft, topRight, center, bottomLeft, bottomRight, bottom
 * @param {string} className - css-класс
 * @example
 * <StandardWidgetLayout/>
 *  <Section place="top">
 *         <div>N2O is awesome</div>
 *     </Section>
 *     <Section place="bottom">
 *         <div>N2O is awesome</div>
 *     </Section>
 *     <Section place="topLeft">
 *         <div>N2O is awesome</div>
 *     </Section>
 *     <Section place="bottomLeft">
 *         <div>N2O is awesome</div>
 *     </Section>
 *     <Section place="bottomRight">
 *         <div>N2O is awesome</div>
 *     </Section>
 *     <Section place="topRight">
 *         <div>N2O is awesome</div>
 *     </Section>
 * </StandardWidgetLayout>
 */
let StandardWidgetLayout = ({ className }) => {
  const classes = cn([
    'n2o-standard-widget-layout',
    {
      [className]: className,
    },
  ]);
  return (
    <div className={classes}>
      <Place
        className={
          'n2o-standard-widget-layout-aside n2o-standard-widget-layout-aside--left'
        }
        name={'left'}
      />
      <div className={'n2o-standard-widget-layout-center'}>
        <div>
          <Place name={'top'} />
        </div>
        <div className={'d-flex justify-content-between'}>
          <Place
            className={
              'n2o-standard-widget-layout-toolbar n2o-standard-widget-layout-toolbar--left'
            }
            name={'topLeft'}
          />
          <Place
            className={
              'n2o-standard-widget-layout-toolbar n2o-standard-widget-layout-toolbar--right'
            }
            name={'topRight'}
          />
        </div>
        <div>
          <Place name={'asideLeft'} />
          <Place name={'center'} />
          <Place name={'asideRight'} />
        </div>
        <div className={'d-flex justify-content-between'}>
          <Place
            className={
              'n2o-standard-widget-layout-toolbar n2o-standard-widget-layout-toolbar--left'
            }
            name={'bottomLeft'}
          />
          <Place
            className={
              'n2o-standard-widget-layout-toolbar n2o-standard-widget-layout-toolbar--right'
            }
            name={'bottomRight'}
          />
        </div>
        <div>
          <Place name={'bottom'} />
        </div>
      </div>
      <Place
        className={
          'n2o-standard-widget-layout-aside n2o-standard-widget-layout-aside--right'
        }
        name={'right'}
      />
    </div>
  );
};

StandardWidgetLayout.propTypes = {
  className: PropTypes.string,
};

StandardWidgetLayout = layoutPlaceResolver(StandardWidgetLayout);
export default StandardWidgetLayout;
