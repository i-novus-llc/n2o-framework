/**
 * Created by emamoshin on 06.10.2017.
 */
import React from 'react';
import PropTypes from 'prop-types';

/**
 * Компонент, содержимое которого(children) будет вставлено в {@link Place}
 * @reactProps {string} place - Place, в который будет вставлены children Секции
 */

class Section extends React.Component {
  render() {
    throw new Error('<Section> можно использовать только внутри <Layout>');
    return null;
  }
}

Section.displayName = 'Section';

Section.propTypes = {
  place: PropTypes.string.isRequired,
};

export default Section;
