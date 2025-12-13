package com.example.projek_map.data;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class KoperasiDatabase_Impl extends KoperasiDatabase {
  private volatile UserDao _userDao;

  private volatile AdminDao _adminDao;

  private volatile PinjamanDao _pinjamanDao;

  private volatile SimpananDao _simpananDao;

  private volatile HistoriPembayaranDao _historiPembayaranDao;

  private volatile HistoriSimpananDao _historiSimpananDao;

  private volatile BuktiPembayaranAnggotaDao _buktiPembayaranAnggotaDao;

  private volatile TransaksiSimpananDao _transaksiSimpananDao;

  private volatile KasTransaksiDao _kasTransaksiDao;

  private volatile PengumumanDao _pengumumanDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `users` (`kodePegawai` TEXT NOT NULL, `email` TEXT NOT NULL, `password` TEXT NOT NULL, `nama` TEXT NOT NULL, `statusKeanggotaan` TEXT NOT NULL, PRIMARY KEY(`kodePegawai`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `admins` (`kodePegawai` TEXT NOT NULL, `email` TEXT NOT NULL, `password` TEXT NOT NULL, `nama` TEXT NOT NULL, `role` TEXT NOT NULL, PRIMARY KEY(`kodePegawai`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `pinjaman` (`id` INTEGER NOT NULL, `kodePegawai` TEXT NOT NULL, `jumlah` INTEGER NOT NULL, `tenor` INTEGER NOT NULL, `status` TEXT NOT NULL, `bunga` REAL NOT NULL, `angsuranTerbayar` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `simpanan` (`kodePegawai` TEXT NOT NULL, `simpananPokok` REAL NOT NULL, `simpananWajib` REAL NOT NULL, `simpananSukarela` REAL NOT NULL, PRIMARY KEY(`kodePegawai`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `histori_pembayaran` (`id` INTEGER NOT NULL, `kodePegawai` TEXT NOT NULL, `pinjamanId` INTEGER NOT NULL, `tanggal` TEXT NOT NULL, `jumlah` INTEGER NOT NULL, `status` TEXT NOT NULL, `buktiPembayaranUri` TEXT, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `histori_simpanan` (`id` INTEGER NOT NULL, `kodePegawai` TEXT NOT NULL, `tanggal` TEXT NOT NULL, `jenis` TEXT NOT NULL, `jumlah` REAL NOT NULL, `keterangan` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `bukti_pembayaran_anggota` (`id` INTEGER NOT NULL, `kodePegawai` TEXT NOT NULL, `uri` TEXT NOT NULL, `tanggal` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `pengumuman` (`id` INTEGER NOT NULL, `judul` TEXT NOT NULL, `isi` TEXT NOT NULL, `tanggal` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `transaksi_simpanan` (`id` INTEGER NOT NULL, `kodePegawai` TEXT NOT NULL, `jenis` TEXT NOT NULL, `jumlah` REAL NOT NULL, `tanggal` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `kas_transaksi` (`id` INTEGER NOT NULL, `tanggal` TEXT NOT NULL, `jenis` TEXT NOT NULL, `kategori` TEXT NOT NULL, `deskripsi` TEXT NOT NULL, `jumlah` REAL NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '278e7b0787ee2d634125c8fe02284da8')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `users`");
        db.execSQL("DROP TABLE IF EXISTS `admins`");
        db.execSQL("DROP TABLE IF EXISTS `pinjaman`");
        db.execSQL("DROP TABLE IF EXISTS `simpanan`");
        db.execSQL("DROP TABLE IF EXISTS `histori_pembayaran`");
        db.execSQL("DROP TABLE IF EXISTS `histori_simpanan`");
        db.execSQL("DROP TABLE IF EXISTS `bukti_pembayaran_anggota`");
        db.execSQL("DROP TABLE IF EXISTS `pengumuman`");
        db.execSQL("DROP TABLE IF EXISTS `transaksi_simpanan`");
        db.execSQL("DROP TABLE IF EXISTS `kas_transaksi`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsUsers = new HashMap<String, TableInfo.Column>(5);
        _columnsUsers.put("kodePegawai", new TableInfo.Column("kodePegawai", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("email", new TableInfo.Column("email", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("password", new TableInfo.Column("password", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("nama", new TableInfo.Column("nama", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("statusKeanggotaan", new TableInfo.Column("statusKeanggotaan", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUsers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUsers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUsers = new TableInfo("users", _columnsUsers, _foreignKeysUsers, _indicesUsers);
        final TableInfo _existingUsers = TableInfo.read(db, "users");
        if (!_infoUsers.equals(_existingUsers)) {
          return new RoomOpenHelper.ValidationResult(false, "users(com.example.projek_map.data.User).\n"
                  + " Expected:\n" + _infoUsers + "\n"
                  + " Found:\n" + _existingUsers);
        }
        final HashMap<String, TableInfo.Column> _columnsAdmins = new HashMap<String, TableInfo.Column>(5);
        _columnsAdmins.put("kodePegawai", new TableInfo.Column("kodePegawai", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAdmins.put("email", new TableInfo.Column("email", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAdmins.put("password", new TableInfo.Column("password", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAdmins.put("nama", new TableInfo.Column("nama", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAdmins.put("role", new TableInfo.Column("role", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAdmins = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAdmins = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAdmins = new TableInfo("admins", _columnsAdmins, _foreignKeysAdmins, _indicesAdmins);
        final TableInfo _existingAdmins = TableInfo.read(db, "admins");
        if (!_infoAdmins.equals(_existingAdmins)) {
          return new RoomOpenHelper.ValidationResult(false, "admins(com.example.projek_map.data.Admin).\n"
                  + " Expected:\n" + _infoAdmins + "\n"
                  + " Found:\n" + _existingAdmins);
        }
        final HashMap<String, TableInfo.Column> _columnsPinjaman = new HashMap<String, TableInfo.Column>(7);
        _columnsPinjaman.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPinjaman.put("kodePegawai", new TableInfo.Column("kodePegawai", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPinjaman.put("jumlah", new TableInfo.Column("jumlah", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPinjaman.put("tenor", new TableInfo.Column("tenor", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPinjaman.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPinjaman.put("bunga", new TableInfo.Column("bunga", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPinjaman.put("angsuranTerbayar", new TableInfo.Column("angsuranTerbayar", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPinjaman = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPinjaman = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPinjaman = new TableInfo("pinjaman", _columnsPinjaman, _foreignKeysPinjaman, _indicesPinjaman);
        final TableInfo _existingPinjaman = TableInfo.read(db, "pinjaman");
        if (!_infoPinjaman.equals(_existingPinjaman)) {
          return new RoomOpenHelper.ValidationResult(false, "pinjaman(com.example.projek_map.data.Pinjaman).\n"
                  + " Expected:\n" + _infoPinjaman + "\n"
                  + " Found:\n" + _existingPinjaman);
        }
        final HashMap<String, TableInfo.Column> _columnsSimpanan = new HashMap<String, TableInfo.Column>(4);
        _columnsSimpanan.put("kodePegawai", new TableInfo.Column("kodePegawai", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSimpanan.put("simpananPokok", new TableInfo.Column("simpananPokok", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSimpanan.put("simpananWajib", new TableInfo.Column("simpananWajib", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSimpanan.put("simpananSukarela", new TableInfo.Column("simpananSukarela", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSimpanan = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSimpanan = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSimpanan = new TableInfo("simpanan", _columnsSimpanan, _foreignKeysSimpanan, _indicesSimpanan);
        final TableInfo _existingSimpanan = TableInfo.read(db, "simpanan");
        if (!_infoSimpanan.equals(_existingSimpanan)) {
          return new RoomOpenHelper.ValidationResult(false, "simpanan(com.example.projek_map.data.Simpanan).\n"
                  + " Expected:\n" + _infoSimpanan + "\n"
                  + " Found:\n" + _existingSimpanan);
        }
        final HashMap<String, TableInfo.Column> _columnsHistoriPembayaran = new HashMap<String, TableInfo.Column>(7);
        _columnsHistoriPembayaran.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHistoriPembayaran.put("kodePegawai", new TableInfo.Column("kodePegawai", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHistoriPembayaran.put("pinjamanId", new TableInfo.Column("pinjamanId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHistoriPembayaran.put("tanggal", new TableInfo.Column("tanggal", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHistoriPembayaran.put("jumlah", new TableInfo.Column("jumlah", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHistoriPembayaran.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHistoriPembayaran.put("buktiPembayaranUri", new TableInfo.Column("buktiPembayaranUri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysHistoriPembayaran = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesHistoriPembayaran = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoHistoriPembayaran = new TableInfo("histori_pembayaran", _columnsHistoriPembayaran, _foreignKeysHistoriPembayaran, _indicesHistoriPembayaran);
        final TableInfo _existingHistoriPembayaran = TableInfo.read(db, "histori_pembayaran");
        if (!_infoHistoriPembayaran.equals(_existingHistoriPembayaran)) {
          return new RoomOpenHelper.ValidationResult(false, "histori_pembayaran(com.example.projek_map.data.HistoriPembayaran).\n"
                  + " Expected:\n" + _infoHistoriPembayaran + "\n"
                  + " Found:\n" + _existingHistoriPembayaran);
        }
        final HashMap<String, TableInfo.Column> _columnsHistoriSimpanan = new HashMap<String, TableInfo.Column>(6);
        _columnsHistoriSimpanan.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHistoriSimpanan.put("kodePegawai", new TableInfo.Column("kodePegawai", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHistoriSimpanan.put("tanggal", new TableInfo.Column("tanggal", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHistoriSimpanan.put("jenis", new TableInfo.Column("jenis", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHistoriSimpanan.put("jumlah", new TableInfo.Column("jumlah", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHistoriSimpanan.put("keterangan", new TableInfo.Column("keterangan", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysHistoriSimpanan = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesHistoriSimpanan = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoHistoriSimpanan = new TableInfo("histori_simpanan", _columnsHistoriSimpanan, _foreignKeysHistoriSimpanan, _indicesHistoriSimpanan);
        final TableInfo _existingHistoriSimpanan = TableInfo.read(db, "histori_simpanan");
        if (!_infoHistoriSimpanan.equals(_existingHistoriSimpanan)) {
          return new RoomOpenHelper.ValidationResult(false, "histori_simpanan(com.example.projek_map.data.HistoriSimpanan).\n"
                  + " Expected:\n" + _infoHistoriSimpanan + "\n"
                  + " Found:\n" + _existingHistoriSimpanan);
        }
        final HashMap<String, TableInfo.Column> _columnsBuktiPembayaranAnggota = new HashMap<String, TableInfo.Column>(4);
        _columnsBuktiPembayaranAnggota.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBuktiPembayaranAnggota.put("kodePegawai", new TableInfo.Column("kodePegawai", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBuktiPembayaranAnggota.put("uri", new TableInfo.Column("uri", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBuktiPembayaranAnggota.put("tanggal", new TableInfo.Column("tanggal", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBuktiPembayaranAnggota = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBuktiPembayaranAnggota = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBuktiPembayaranAnggota = new TableInfo("bukti_pembayaran_anggota", _columnsBuktiPembayaranAnggota, _foreignKeysBuktiPembayaranAnggota, _indicesBuktiPembayaranAnggota);
        final TableInfo _existingBuktiPembayaranAnggota = TableInfo.read(db, "bukti_pembayaran_anggota");
        if (!_infoBuktiPembayaranAnggota.equals(_existingBuktiPembayaranAnggota)) {
          return new RoomOpenHelper.ValidationResult(false, "bukti_pembayaran_anggota(com.example.projek_map.data.BuktiPembayaranAnggota).\n"
                  + " Expected:\n" + _infoBuktiPembayaranAnggota + "\n"
                  + " Found:\n" + _existingBuktiPembayaranAnggota);
        }
        final HashMap<String, TableInfo.Column> _columnsPengumuman = new HashMap<String, TableInfo.Column>(4);
        _columnsPengumuman.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPengumuman.put("judul", new TableInfo.Column("judul", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPengumuman.put("isi", new TableInfo.Column("isi", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPengumuman.put("tanggal", new TableInfo.Column("tanggal", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPengumuman = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPengumuman = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPengumuman = new TableInfo("pengumuman", _columnsPengumuman, _foreignKeysPengumuman, _indicesPengumuman);
        final TableInfo _existingPengumuman = TableInfo.read(db, "pengumuman");
        if (!_infoPengumuman.equals(_existingPengumuman)) {
          return new RoomOpenHelper.ValidationResult(false, "pengumuman(com.example.projek_map.data.Pengumuman).\n"
                  + " Expected:\n" + _infoPengumuman + "\n"
                  + " Found:\n" + _existingPengumuman);
        }
        final HashMap<String, TableInfo.Column> _columnsTransaksiSimpanan = new HashMap<String, TableInfo.Column>(5);
        _columnsTransaksiSimpanan.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransaksiSimpanan.put("kodePegawai", new TableInfo.Column("kodePegawai", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransaksiSimpanan.put("jenis", new TableInfo.Column("jenis", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransaksiSimpanan.put("jumlah", new TableInfo.Column("jumlah", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransaksiSimpanan.put("tanggal", new TableInfo.Column("tanggal", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTransaksiSimpanan = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTransaksiSimpanan = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTransaksiSimpanan = new TableInfo("transaksi_simpanan", _columnsTransaksiSimpanan, _foreignKeysTransaksiSimpanan, _indicesTransaksiSimpanan);
        final TableInfo _existingTransaksiSimpanan = TableInfo.read(db, "transaksi_simpanan");
        if (!_infoTransaksiSimpanan.equals(_existingTransaksiSimpanan)) {
          return new RoomOpenHelper.ValidationResult(false, "transaksi_simpanan(com.example.projek_map.data.TransaksiSimpanan).\n"
                  + " Expected:\n" + _infoTransaksiSimpanan + "\n"
                  + " Found:\n" + _existingTransaksiSimpanan);
        }
        final HashMap<String, TableInfo.Column> _columnsKasTransaksi = new HashMap<String, TableInfo.Column>(6);
        _columnsKasTransaksi.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsKasTransaksi.put("tanggal", new TableInfo.Column("tanggal", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsKasTransaksi.put("jenis", new TableInfo.Column("jenis", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsKasTransaksi.put("kategori", new TableInfo.Column("kategori", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsKasTransaksi.put("deskripsi", new TableInfo.Column("deskripsi", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsKasTransaksi.put("jumlah", new TableInfo.Column("jumlah", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysKasTransaksi = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesKasTransaksi = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoKasTransaksi = new TableInfo("kas_transaksi", _columnsKasTransaksi, _foreignKeysKasTransaksi, _indicesKasTransaksi);
        final TableInfo _existingKasTransaksi = TableInfo.read(db, "kas_transaksi");
        if (!_infoKasTransaksi.equals(_existingKasTransaksi)) {
          return new RoomOpenHelper.ValidationResult(false, "kas_transaksi(com.example.projek_map.data.KasTransaksi).\n"
                  + " Expected:\n" + _infoKasTransaksi + "\n"
                  + " Found:\n" + _existingKasTransaksi);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "278e7b0787ee2d634125c8fe02284da8", "44104c40395e4646cd796fe9376c020b");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "users","admins","pinjaman","simpanan","histori_pembayaran","histori_simpanan","bukti_pembayaran_anggota","pengumuman","transaksi_simpanan","kas_transaksi");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `users`");
      _db.execSQL("DELETE FROM `admins`");
      _db.execSQL("DELETE FROM `pinjaman`");
      _db.execSQL("DELETE FROM `simpanan`");
      _db.execSQL("DELETE FROM `histori_pembayaran`");
      _db.execSQL("DELETE FROM `histori_simpanan`");
      _db.execSQL("DELETE FROM `bukti_pembayaran_anggota`");
      _db.execSQL("DELETE FROM `pengumuman`");
      _db.execSQL("DELETE FROM `transaksi_simpanan`");
      _db.execSQL("DELETE FROM `kas_transaksi`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(UserDao.class, UserDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(AdminDao.class, AdminDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PinjamanDao.class, PinjamanDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SimpananDao.class, SimpananDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(HistoriPembayaranDao.class, HistoriPembayaranDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(HistoriSimpananDao.class, HistoriSimpananDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(BuktiPembayaranAnggotaDao.class, BuktiPembayaranAnggotaDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TransaksiSimpananDao.class, TransaksiSimpananDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(KasTransaksiDao.class, KasTransaksiDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PengumumanDao.class, PengumumanDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public UserDao userDao() {
    if (_userDao != null) {
      return _userDao;
    } else {
      synchronized(this) {
        if(_userDao == null) {
          _userDao = new UserDao_Impl(this);
        }
        return _userDao;
      }
    }
  }

  @Override
  public AdminDao adminDao() {
    if (_adminDao != null) {
      return _adminDao;
    } else {
      synchronized(this) {
        if(_adminDao == null) {
          _adminDao = new AdminDao_Impl(this);
        }
        return _adminDao;
      }
    }
  }

  @Override
  public PinjamanDao pinjamanDao() {
    if (_pinjamanDao != null) {
      return _pinjamanDao;
    } else {
      synchronized(this) {
        if(_pinjamanDao == null) {
          _pinjamanDao = new PinjamanDao_Impl(this);
        }
        return _pinjamanDao;
      }
    }
  }

  @Override
  public SimpananDao simpananDao() {
    if (_simpananDao != null) {
      return _simpananDao;
    } else {
      synchronized(this) {
        if(_simpananDao == null) {
          _simpananDao = new SimpananDao_Impl(this);
        }
        return _simpananDao;
      }
    }
  }

  @Override
  public HistoriPembayaranDao historiPembayaranDao() {
    if (_historiPembayaranDao != null) {
      return _historiPembayaranDao;
    } else {
      synchronized(this) {
        if(_historiPembayaranDao == null) {
          _historiPembayaranDao = new HistoriPembayaranDao_Impl(this);
        }
        return _historiPembayaranDao;
      }
    }
  }

  @Override
  public HistoriSimpananDao historiSimpananDao() {
    if (_historiSimpananDao != null) {
      return _historiSimpananDao;
    } else {
      synchronized(this) {
        if(_historiSimpananDao == null) {
          _historiSimpananDao = new HistoriSimpananDao_Impl(this);
        }
        return _historiSimpananDao;
      }
    }
  }

  @Override
  public BuktiPembayaranAnggotaDao buktiPembayaranAnggotaDao() {
    if (_buktiPembayaranAnggotaDao != null) {
      return _buktiPembayaranAnggotaDao;
    } else {
      synchronized(this) {
        if(_buktiPembayaranAnggotaDao == null) {
          _buktiPembayaranAnggotaDao = new BuktiPembayaranAnggotaDao_Impl(this);
        }
        return _buktiPembayaranAnggotaDao;
      }
    }
  }

  @Override
  public TransaksiSimpananDao transaksiSimpananDao() {
    if (_transaksiSimpananDao != null) {
      return _transaksiSimpananDao;
    } else {
      synchronized(this) {
        if(_transaksiSimpananDao == null) {
          _transaksiSimpananDao = new TransaksiSimpananDao_Impl(this);
        }
        return _transaksiSimpananDao;
      }
    }
  }

  @Override
  public KasTransaksiDao kasTransaksiDao() {
    if (_kasTransaksiDao != null) {
      return _kasTransaksiDao;
    } else {
      synchronized(this) {
        if(_kasTransaksiDao == null) {
          _kasTransaksiDao = new KasTransaksiDao_Impl(this);
        }
        return _kasTransaksiDao;
      }
    }
  }

  @Override
  public PengumumanDao pengumumanDao() {
    if (_pengumumanDao != null) {
      return _pengumumanDao;
    } else {
      synchronized(this) {
        if(_pengumumanDao == null) {
          _pengumumanDao = new PengumumanDao_Impl(this);
        }
        return _pengumumanDao;
      }
    }
  }
}
