import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { map, memoize } from 'lodash';

import { getModelSelector } from '../../../selectors/models';
import propsResolver from '../../../utils/propsResolver';

/**
 * Контейнер для {@link Breadcrumb}
 * @reactProps {object} items -массив из {label, href} объектов, описывающих одну вкладку ("крошку")
 *
 * @example
 *  const items = [
 *    {
 *      href: '/test1',
 *      label: 'test1'
 *    },
 *    {
 *      href: '/test2',
 *      label: 'test2'
 *    },
 *  ]
 * <Breadcrumb  items={items}/>
 * */
class BreadcrumbContainer extends React.Component {
  constructor(props) {
    super(props);
  }

  /**
   * Базовый рендер
   * @returns {*}
   */
  render() {
    const DefaultBreadcrumb = this.props.defaultBreadcrumb;
    return (
      <React.Fragment>
        <DefaultBreadcrumb items={this.props.items} />
      </React.Fragment>
    );
  }
}

BreadcrumbContainer.propTypes = {
  defaultBreadcrumb: PropTypes.node,
  items: PropTypes.arrayOf(
    PropTypes.shape({
      label: PropTypes.string,
      modelLink: PropTypes.string,
      path: PropTypes.string
    })
  )
};

BreadcrumbContainer.defaultProps = {
  items: [],
  defaultBreadcrumb: () => null
};

const memoizedMap = memoize(map);

const mapStateToProps = (state, ownProps) => {
  return {
    items: memoizedMap(ownProps.items, item => {
      if (item.modelLink) {
        return {
          ...item,
          label: propsResolver(item.label, getModelSelector(item.modelLink.link)(state))
        };
      }

      return item;
    })
  };
};

BreadcrumbContainer = connect(mapStateToProps)(BreadcrumbContainer);
export default BreadcrumbContainer;
