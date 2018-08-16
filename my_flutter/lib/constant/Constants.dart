import 'package:flutter/material.dart';
import 'package:event_bus/event_bus.dart';

class AppColors {
  static Color colorPrimary = const Color(0xff0054dd);
  static Color accentColor = colorPrimary;
  static Color deviderBg = const Color(0xffececec);
}

class Constants {
  static final String endLineTag = "COMPLETE";

  static EventBus eventBus = new EventBus();
}
