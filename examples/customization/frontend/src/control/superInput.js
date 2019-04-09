/**
 * Created by emamoshin on 15.08.2017.
 */
import React from 'react';
import PropTypes from 'prop-types';

class SuperInput extends React.Component {

  render() {
    return (
      <input type="password"
        className="form-control" placeholder="Компонент пароль" />
    );
  }

}

export default SuperInput;
