import 'package:flutter/material.dart';
import 'package:event_bus/event_bus.dart';

class AppColors {
  static Color colorPrimary = const Color(0xff0054dd);
  static Color accentColor = colorPrimary;
}

class Constants {
  static final String endLineTag = "COMPLETE";

  static EventBus eventBus = new EventBus();
}
